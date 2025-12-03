using System.Collections;
using UnityEngine;
using UnityEngine.Networking;
using System.Text;

// Classes auxiliares para transformar JSON em Objetos C#
[System.Serializable]
public class SessionResponse
{
    public string id;
    public string operatorId;
    public double currentScore;
    public bool active;
}

[System.Serializable]
public class TelemetryInput
{
    public string sessionId;
    public double speed;
    public bool collision;
}

public class NetworkManager : MonoBehaviour
{
    // Singleton: permite que outros scripts acessem este sem precisar de "fios"
    public static NetworkManager Instance;

    private string apiUrl = "http://localhost:8080/api/v1/training";
    private string currentSessionId;
    private bool isSessionActive = false;

    void Awake()
    {
        // Garante que só existe um Gerente de Rede na cena
        if (Instance == null) Instance = this;
    }

    void Start()
    {
        // Ao dar Play, tenta conectar no Java automaticamente
        StartCoroutine(StartSession("PedroOperador"));
    }

    // Rotina para iniciar a sessão (GET /start)
    IEnumerator StartSession(string operatorId)
    {
        string url = $"{apiUrl}/start/{operatorId}";

        // Envia um POST vazio apenas para criar a sessão
        using (UnityWebRequest www = UnityWebRequest.Post(url, new WWWForm()))
        {
            yield return www.SendWebRequest();

            if (www.result != UnityWebRequest.Result.Success)
            {
                Debug.LogError("Erro ao conectar no Java: " + www.error);
            }
            else
            {
                string jsonResult = www.downloadHandler.text;
                Debug.Log("Backend respondeu: " + jsonResult);

                // Transforma o texto JSON em objeto C#
                SessionResponse response = JsonUtility.FromJson<SessionResponse>(jsonResult);
                currentSessionId = response.id;
                isSessionActive = true;
                Debug.Log("Sessão iniciada! ID: " + currentSessionId);
            }
        }
    }

    // Método público que o carro vai chamar
    public void SendTelemetry(double speed, bool collision)
    {
        if (!isSessionActive) return;

        StartCoroutine(PostTelemetry(speed, collision));
    }

    // Rotina de envio de dados (POST /telemetry)
    IEnumerator PostTelemetry(double speed, bool collision)
    {
        TelemetryInput data = new TelemetryInput();
        data.sessionId = currentSessionId;
        data.speed = speed;
        data.collision = collision;

        // Transforma objeto C# em texto JSON
        string json = JsonUtility.ToJson(data);

        using (UnityWebRequest www = new UnityWebRequest($"{apiUrl}/telemetry", "POST"))
        {
            byte[] bodyRaw = Encoding.UTF8.GetBytes(json);
            www.uploadHandler = new UploadHandlerRaw(bodyRaw);
            www.downloadHandler = new DownloadHandlerBuffer();
            www.SetRequestHeader("Content-Type", "application/json");

            yield return www.SendWebRequest();

            if (www.result != UnityWebRequest.Result.Success)
            {
                Debug.LogError("Erro de envio: " + www.error);
            }
        }
    }
}