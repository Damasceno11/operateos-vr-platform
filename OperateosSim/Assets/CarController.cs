using UnityEngine;

public class CarController : MonoBehaviour
{
    public float moveSpeed = 10f;
    public float rotationSpeed = 100f;

    private Rigidbody rb;
    private float currentSpeedKMH;

    // Timer para enviar dados a cada 1 segundo (evita travar a rede)
    private float telemetryTimer = 0f;
    private float telemetryInterval = 1.0f;

    void Start()
    {
        // Pega o componente de física do cubo
        rb = GetComponent<Rigidbody>();
    }

    void Update()
    {
        HandleMovement();
        HandleTelemetry();
    }

    void HandleMovement()
    {
        // W/S ou Setas Cima/Baixo
        float translation = Input.GetAxis("Vertical") * moveSpeed * Time.deltaTime;
        // A/D ou Setas Esq/Dir
        float rotation = Input.GetAxis("Horizontal") * rotationSpeed * Time.deltaTime;

        transform.Translate(0, 0, translation);
        transform.Rotate(0, rotation, 0);

        // Simula velocidade baseada no quanto você aperta o botão
        if (translation != 0)
            currentSpeedKMH = Mathf.Abs(translation) * 500f; // Valor fictício para teste
        else
            currentSpeedKMH = 0;
    }

    void HandleTelemetry()
    {
        telemetryTimer += Time.deltaTime;
        if (telemetryTimer >= telemetryInterval)
        {
            // Envia "Tudo ok, velocidade X, sem colisão"
            NetworkManager.Instance.SendTelemetry(currentSpeedKMH, false);
            telemetryTimer = 0f;
        }
    }

    // DETECÇÃO DE COLISÃO
    void OnCollisionEnter(Collision collision)
    {
        // Verifica se bateu em algo com a etiqueta "Obstaculo"
        if (collision.gameObject.CompareTag("Obstaculo"))
        {
            Debug.Log("BATEU NO OBSTÁCULO! Enviando alerta crítico...");

            // Envia "COLISÃO REAL"
            NetworkManager.Instance.SendTelemetry(currentSpeedKMH, true);
        }
    }
}