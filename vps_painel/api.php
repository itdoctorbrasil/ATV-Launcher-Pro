<?php
header('Content-Type: application/json; charset=utf-8');
header('Access-Control-Allow-Origin: *');
header('Access-Control-Allow-Methods: GET, POST, OPTIONS');
header('Access-Control-Allow-Headers: Content-Type, Authorization');

if ($_SERVER['REQUEST_METHOD'] === 'OPTIONS') {
    http_response_code(200);
    exit;
}

$dataFile = __DIR__ . '/config.json';
if (!file_exists($dataFile)) {
    $initialConfig = [
        "app_name" => "ATV Launcher",
        "logo_text" => "LOGO CUSTOMIZADA",
        "logo_url" => "",
        "video_url" => "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4",
        "ota" => [
            "version" => "1.0.1",
            "version_code" => 2,
            "apk_url" => "http://177.153.68.5/painel/updates/atv-launcher-latest.apk",
            "changelog" => "Otimizações de performance para controle remoto e D-PAD."
        ],
        "dock_apps" => [
            ["id" => "1", "name" => "UniTV", "package" => "com.unitv.app", "type" => "unitv"],
            ["id" => "2", "name" => "Google Play", "package" => "com.android.vending", "type" => "google_play"],
            ["id" => "3", "name" => "Prime Video", "package" => "com.amazon.amazonvideo.livingroom", "type" => "prime_video"],
            ["id" => "4", "name" => "Netflix", "package" => "com.netflix.ninja", "type" => "netflix"],
            ["id" => "5", "name" => "YouTube TV", "package" => "com.google.android.youtube.tv", "type" => "youtube_tv"]
        ]
    ];
    file_put_contents($dataFile, json_encode($initialConfig, JSON_PRETTY_PRINT | JSON_UNESCAPED_SLASHES | JSON_UNESCAPED_UNICODE));
}

$action = $_GET['action'] ?? 'get_config';

if ($action === 'get_config') {
    echo file_get_contents($dataFile);
    exit;
}

if ($action === 'check_ota') {
    $data = json_decode(file_get_contents($dataFile), true);
    echo json_encode($data['ota']);
    exit;
}

if ($action === 'save_config' && $_SERVER['REQUEST_METHOD'] === 'POST') {
    $input = json_decode(file_get_contents('php://input'), true);
    if ($input) {
        file_put_contents($dataFile, json_encode($input, JSON_PRETTY_PRINT | JSON_UNESCAPED_SLASHES | JSON_UNESCAPED_UNICODE));
        echo json_encode(["status" => "success", "message" => "Configuração atualizada no VPS!"]);
        exit;
    }
}

echo json_encode(["status" => "error", "message" => "Ação inválida"]);
