<?php
session_start();

$admin_user = "administrador";
$admin_pass = "2b079e541af29b37f713";

$is_logged = isset($_SESSION['user']);
$error = "";

if (isset($_POST['login'])) {
    $u = trim($_POST['username'] ?? '');
    $p = trim($_POST['password'] ?? '');
    if (($u === $admin_user && $p === $admin_pass) || ($u === 'Login teste' && $p === 'teste') || ($u === 'revendedor' && $p === '123456')) {
        $_SESSION['user'] = $u;
        $_SESSION['role'] = ($u === $admin_user) ? 'admin' : 'revendedor';
        header("Location: index.php");
        exit;
    } else {
        $error = "Credenciais inválidas. Verifique usuário e senha.";
    }
}

if (isset($_GET['logout'])) {
    session_destroy();
    header("Location: index.php");
    exit;
}

$currentUser = $_SESSION['user'] ?? 'Login teste';
$currentEmail = ($currentUser === 'administrador') ? 'admin@atvbrasil.com' : 'login.teste@gmail.com';
?>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Painel ATV Launcher - Painel de Revendedor</title>
    <style>
        :root {
            --sidebar-bg: #070B19;
            --sidebar-hover: #12182D;
            --main-bg: #F0F4FA;
            --card-bg: #FFFFFF;
            --text-dark: #1E293B;
            --text-muted: #64748B;
            --sidebar-text: #94A3B8;
            --sidebar-text-active: #FFFFFF;
            --primary-purple: #7C3AED;
            --primary-gradient: linear-gradient(135deg, #8B5CF6 0%, #6D28D9 100%);
            --accent-blue: #3B82F6;
            --border-light: #E2E8F0;
            --border-sidebar: #1E2640;
            --badge-orange: #FFF7ED;
            --badge-orange-border: #FDBA74;
            --badge-orange-text: #EA580C;
            --badge-green-bg: #DCFCE7;
            --badge-green-text: #16A34A;
            --shadow-card: 0 4px 16px rgba(0, 0, 0, 0.04);
            --shadow-active-menu: 0 4px 14px rgba(124, 58, 237, 0.35);
        }

        body.dark-theme {
            --main-bg: #0B0F1F;
            --card-bg: #131A2E;
            --text-dark: #F8FAFC;
            --text-muted: #94A3B8;
            --border-light: #242E49;
            --shadow-card: 0 4px 20px rgba(0, 0, 0, 0.3);
            --badge-orange: #3D2214;
            --badge-orange-border: #9A3412;
            --badge-orange-text: #FB923C;
        }

        * { box-sizing: border-box; margin: 0; padding: 0; font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, Helvetica, Arial, sans-serif; }
        body { background: var(--main-bg); color: var(--text-dark); min-height: 100vh; display: flex; transition: background 0.2s, color 0.2s; }

        /* Login Layout */
        .login-wrap { width: 100%; display: flex; align-items: center; justify-content: center; min-height: 100vh; background: #070B19; padding: 20px; }
        .login-card { width: 100%; max-width: 440px; background: #131A2E; border: 1px solid #242E49; border-radius: 20px; padding: 36px; box-shadow: 0 20px 40px rgba(0,0,0,0.5); text-align: center; }
        .login-brand { display: flex; align-items: center; justify-content: center; gap: 12px; margin-bottom: 24px; }
        .login-logo-badge { width: 46px; height: 46px; border-radius: 12px; background: linear-gradient(135deg, #A855F7, #6D28D9); color: white; display: flex; align-items: center; justify-content: center; font-size: 24px; font-weight: 900; box-shadow: 0 4px 12px rgba(168, 85, 247, 0.4); }
        .login-title { font-size: 22px; font-weight: 800; color: #FFFFFF; text-align: left; line-height: 1.2; }
        .login-title span { color: #818CF8; font-weight: 500; }
        .login-sub { font-size: 13px; color: #94A3B8; margin-top: 4px; }
        .form-group { text-align: left; margin-bottom: 16px; }
        .form-group label { display: block; font-size: 12px; font-weight: 600; color: #94A3B8; margin-bottom: 6px; }
        .form-control { width: 100%; padding: 12px 14px; border-radius: 10px; border: 1px solid #242E49; background: #0A0F1D; color: #FFFFFF; font-size: 14px; outline: none; }
        .form-control:focus { border-color: #8B5CF6; }
        .btn-submit { width: 100%; padding: 13px; border-radius: 10px; border: none; background: var(--primary-gradient); color: #FFFFFF; font-size: 14px; font-weight: 700; cursor: pointer; box-shadow: 0 4px 14px rgba(124,58,237,0.4); margin-top: 8px; }

        /* Main SaaS Layout */
        .app-container { display: flex; width: 100%; min-height: 100vh; }

        /* Sidebar */
        .sidebar { width: 280px; min-width: 280px; background: var(--sidebar-bg); border-right: 1px solid var(--border-sidebar); display: flex; flex-direction: column; padding: 24px 16px; }
        .sidebar-brand { display: flex; align-items: center; gap: 14px; padding: 0 8px 24px 8px; }
        .brand-badge { width: 44px; height: 44px; border-radius: 12px; background: linear-gradient(135deg, #A855F7 0%, #6D28D9 100%); color: #FFF; display: flex; align-items: center; justify-content: center; font-size: 22px; font-weight: 900; box-shadow: 0 4px 12px rgba(168, 85, 247, 0.4); }
        .brand-info h2 { font-size: 19px; font-weight: 800; color: #FFFFFF; letter-spacing: -0.3px; }
        .brand-info h2 span { color: #818CF8; font-weight: 600; }
        .brand-info p { font-size: 12px; color: #64748B; margin-top: 1px; }

        /* User Profile Card */
        .account-card { background: rgba(18, 24, 45, 0.7); border: 1px solid var(--border-sidebar); border-radius: 14px; padding: 14px; margin-bottom: 22px; }
        .account-header { display: flex; align-items: center; justify-content: space-between; margin-bottom: 8px; }
        .account-badge-tag { display: flex; align-items: center; gap: 6px; font-size: 11px; font-weight: 700; color: #94A3B8; text-transform: uppercase; letter-spacing: 0.5px; }
        .status-dot { width: 8px; height: 8px; border-radius: 50%; background: #10B981; box-shadow: 0 0 8px #10B981; }
        .account-name { font-size: 13px; font-weight: 600; color: #F8FAFC; }
        .account-email { font-size: 11px; color: #64748B; margin-top: 2px; }

        /* Sidebar Nav */
        .nav-menu { display: flex; flex-direction: column; gap: 6px; flex: 1; }
        .nav-item { display: flex; align-items: center; gap: 14px; padding: 12px 18px; border-radius: 14px; color: var(--sidebar-text); text-decoration: none; font-size: 14px; font-weight: 500; cursor: pointer; transition: all 0.15s ease; border: 1px solid transparent; }
        .nav-item svg { width: 20px; height: 20px; stroke: currentColor; fill: none; stroke-width: 2; flex-shrink: 0; }
        .nav-item:hover { background: var(--sidebar-hover); color: #E2E8F0; }
        .nav-item.active { background: var(--primary-gradient); color: #FFFFFF; font-weight: 600; box-shadow: var(--shadow-active-menu); }
        .nav-item.active svg { stroke: #FFFFFF; }

        /* Main Content */
        .main-content { flex: 1; display: flex; flex-direction: column; overflow-y: auto; }
        
        /* Top bar */
        .top-bar { display: flex; align-items: center; justify-content: space-between; padding: 20px 36px 12px 36px; }
        .page-header-title { display: flex; align-items: center; gap: 12px; }
        .page-header-title h1 { font-size: 22px; font-weight: 800; color: var(--text-dark); letter-spacing: -0.5px; }
        .badge-user-pill { background: var(--badge-orange); border: 1px solid var(--badge-orange-border); color: var(--badge-orange-text); font-size: 13px; font-weight: 700; padding: 4px 14px; border-radius: 20px; }
        .page-subtitle { font-size: 13px; color: var(--text-muted); margin-top: 4px; }
        
        .header-actions { display: flex; align-items: center; gap: 12px; }
        .theme-toggle-btn { width: 40px; height: 40px; border-radius: 12px; border: 1px solid var(--border-light); background: var(--card-bg); cursor: pointer; display: flex; align-items: center; justify-content: center; color: var(--text-muted); box-shadow: var(--shadow-card); }
        .logout-btn { padding: 8px 16px; border-radius: 10px; border: 1px solid var(--border-light); background: var(--card-bg); color: var(--text-dark); text-decoration: none; font-size: 12px; font-weight: 600; }

        /* Container */
        .content-body { padding: 16px 36px 36px 36px; flex: 1; }

        /* Panels / Section Views */
        .view-panel { display: none; }
        .view-panel.active { display: block; animation: fadeIn 0.15s ease-in; }
        @keyframes fadeIn { from { opacity: 0; transform: translateY(4px); } to { opacity: 1; transform: translateY(0); } }

        /* Big Card Container */
        .content-card { background: var(--card-bg); border: 1px solid var(--border-light); border-radius: 20px; padding: 28px; box-shadow: var(--shadow-card); margin-bottom: 24px; }
        .tag-pill { display: inline-block; background: #EDE9FE; color: #6D28D9; font-size: 11px; font-weight: 800; padding: 4px 12px; border-radius: 12px; text-transform: uppercase; letter-spacing: 0.5px; margin-bottom: 12px; }
        .content-card h2 { font-size: 24px; font-weight: 800; color: var(--text-dark); margin-bottom: 4px; }
        .content-card p.desc { font-size: 13px; color: var(--text-muted); margin-bottom: 24px; }

        /* Stats Row */
        .stats-grid { display: grid; grid-template-columns: repeat(3, 1fr); gap: 20px; margin-top: 10px; }
        .stat-card { background: var(--card-bg); border: 1px solid var(--border-light); border-radius: 16px; padding: 22px; box-shadow: var(--shadow-card); }
        .stat-title { font-size: 11px; font-weight: 800; letter-spacing: 0.8px; text-transform: uppercase; margin-bottom: 12px; }
        .stat-title.blue { color: #3B82F6; }
        .stat-title.green { color: #10B981; }
        .stat-title.red { color: #EF4444; }
        .stat-number { font-size: 38px; font-weight: 900; color: var(--text-dark); line-height: 1; margin-bottom: 8px; }
        .stat-desc { font-size: 12px; color: var(--text-muted); }

        /* Buttons */
        .btn-purple { display: inline-flex; align-items: center; gap: 8px; background: var(--primary-gradient); color: #FFFFFF; padding: 12px 22px; border-radius: 12px; font-size: 13px; font-weight: 700; border: none; cursor: pointer; text-decoration: none; box-shadow: 0 4px 14px rgba(124, 58, 237, 0.35); }
        .btn-purple:hover { opacity: 0.95; }

        /* Search & Filters */
        .toolbar-row { display: flex; align-items: center; justify-content: space-between; gap: 16px; margin-bottom: 20px; flex-wrap: wrap; }
        .search-box { display: flex; align-items: center; gap: 10px; background: var(--card-bg); border: 1px solid var(--border-light); border-radius: 24px; padding: 10px 18px; flex: 1; max-width: 440px; box-shadow: var(--shadow-card); }
        .search-box svg { width: 18px; height: 18px; stroke: var(--text-muted); }
        .search-box input { border: none; background: transparent; outline: none; width: 100%; font-size: 13px; color: var(--text-dark); }
        .filter-group { display: flex; align-items: center; gap: 8px; }
        .filter-btn { padding: 7px 16px; border-radius: 20px; border: 1px solid var(--border-light); background: var(--card-bg); font-size: 12px; font-weight: 600; color: var(--text-muted); cursor: pointer; }
        .filter-btn.active { background: #4F46E5; color: #FFFFFF; border-color: #4F46E5; }
        .view-btn { padding: 7px 14px; border-radius: 8px; border: 1px solid var(--border-light); background: var(--card-bg); font-size: 12px; font-weight: 600; color: var(--text-muted); cursor: pointer; }
        .view-btn.active { background: #3B82F6; color: #FFFFFF; border-color: #3B82F6; }

        /* Table */
        .table-responsive { width: 100%; overflow-x: auto; }
        table.clients-table { width: 100%; border-collapse: collapse; text-align: left; }
        table.clients-table th { padding: 14px 16px; font-size: 11px; font-weight: 800; color: #94A3B8; text-transform: uppercase; letter-spacing: 0.8px; border-bottom: 1px solid var(--border-light); }
        table.clients-table td { padding: 16px; border-bottom: 1px solid var(--border-light); font-size: 13px; vertical-align: middle; }
        .mac-col { color: #2563EB; font-weight: 700; font-family: monospace; font-size: 13px; }
        .token-col { color: #C2410C; font-weight: 700; font-family: monospace; font-size: 13px; }
        .date-pill { display: inline-block; padding: 4px 12px; border-radius: 12px; border: 1px solid #CBD5E1; background: var(--main-bg); font-size: 11px; font-weight: 700; color: var(--text-dark); }
        .status-pill { display: inline-block; padding: 4px 12px; border-radius: 14px; font-size: 11px; font-weight: 800; background: var(--badge-green-bg); color: var(--badge-green-text); text-transform: uppercase; }
        .actions-col { display: flex; align-items: center; gap: 8px; }
        .action-icon-btn { width: 32px; height: 32px; border-radius: 8px; display: flex; align-items: center; justify-content: center; cursor: pointer; border: none; }
        .action-icon-btn.edit { background: #1E293B; color: #FFFFFF; }
        .action-icon-btn.delete { background: #FFE4E6; color: #E11D48; }

        /* Logo Page Layout (Matching LOGO.png) */
        .logo-layout-grid { display: grid; grid-template-columns: 1.1f 0.9fr; gap: 28px; margin-top: 16px; }
        .upload-dropzone { border: 2px dashed #CBD5E1; border-radius: 16px; padding: 44px 20px; text-align: center; background: var(--main-bg); cursor: pointer; transition: border-color 0.2s; }
        .upload-dropzone:hover { border-color: #7C3AED; }
        .upload-dropzone svg { width: 44px; height: 44px; stroke: #6366F1; margin-bottom: 12px; }
        .upload-dropzone h4 { font-size: 14px; font-weight: 700; color: var(--text-dark); margin-bottom: 4px; }
        .upload-dropzone p { font-size: 12px; color: var(--text-muted); }
        .preview-box-card { background: var(--main-bg); border: 1px solid var(--border-light); border-radius: 16px; padding: 28px; text-align: center; }
        .preview-box-card h5 { font-size: 11px; font-weight: 800; letter-spacing: 0.8px; color: #94A3B8; text-transform: uppercase; margin-bottom: 18px; }
        .preview-image-container { background: #FFFFFF; border: 1px solid #E2E8F0; border-radius: 14px; padding: 24px; min-height: 120px; display: flex; align-items: center; justify-content: center; margin-bottom: 16px; box-shadow: 0 2px 10px rgba(0,0,0,0.02); }
        .preview-badge-status { color: #10B981; font-size: 12px; font-weight: 700; display: flex; align-items: center; justify-content: center; gap: 6px; }
        .btn-dark-save { background: #131A2E; color: #FFFFFF; padding: 13px 28px; border-radius: 12px; border: none; font-size: 14px; font-weight: 700; cursor: pointer; width: 100%; margin-top: 18px; }

        /* General Forms */
        .grid-options { display: grid; grid-template-columns: repeat(auto-fit, minmax(130px, 1fr)); gap: 14px; margin-top: 14px; }
        .theme-color-pill { padding: 14px; border-radius: 12px; border: 2px solid transparent; text-align: center; cursor: pointer; }
        .theme-color-pill.active { border-color: #7C3AED; }
    </style>
</head>
<body>

<?php if (!$is_logged): ?>
    <div class="login-wrap">
        <div class="login-card">
            <div class="login-brand">
                <div class="login-logo-badge">L</div>
                <div>
                    <h2 class="login-title">ATV <span>LAUNCHER</span></h2>
                    <p class="login-sub">Painel de Revendedor • VPS 177.153.68.5</p>
                </div>
            </div>

            <?php if ($error): ?>
                <div style="background:#7F1D1D; color:#FCA5A5; padding:10px 14px; border-radius:10px; font-size:12px; margin-bottom:16px;"><?= htmlspecialchars($error) ?></div>
            <?php endif; ?>

            <form method="POST">
                <div class="form-group">
                    <label>Usuário de Acesso</label>
                    <input type="text" name="username" class="form-control" value="administrador" required>
                </div>
                <div class="form-group">
                    <label>Senha</label>
                    <input type="password" name="password" class="form-control" value="2b079e541af29b37f713" required>
                </div>
                <button type="submit" name="login" class="btn-submit">Acessar Painel de Controle</button>
            </form>
            <div style="margin-top:20px; font-size:12px; color:#64748B;">
                Login de demonstração revenda: <b>Login teste</b> / senha: <b>teste</b>
            </div>
        </div>
    </div>
<?php else: ?>

    <div class="app-container">
        <!-- SIDEBAR LATERAL (Conforme Imagens) -->
        <aside class="sidebar">
            <div class="sidebar-brand">
                <div class="brand-badge">L</div>
                <div class="brand-info">
                    <h2>ATV <span>LAUNCHER</span></h2>
                    <p>Painel de Revendedor</p>
                </div>
            </div>

            <!-- Conta Revenda Card -->
            <div class="account-card">
                <div class="account-header">
                    <div class="account-badge-tag">
                        <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M12 22s8-4 8-10V5l-8-3-8 3v7c0 6 8 10 8 10z"/></svg>
                        CONTA REVENDA
                    </div>
                    <div class="status-dot"></div>
                </div>
                <div class="account-name">Usuário: <?= htmlspecialchars($currentUser) ?></div>
                <div class="account-email">E-mail: <?= htmlspecialchars($currentEmail) ?></div>
            </div>

            <!-- 8 MENUS LATERAIS SOLICITADOS -->
            <nav class="nav-menu">
                <a class="nav-item active" onclick="switchView('visao-geral', this)">
                    <svg viewBox="0 0 24 24"><rect x="3" y="3" width="7" height="7"/><rect x="14" y="3" width="7" height="7"/><rect x="14" y="14" width="7" height="7"/><rect x="3" y="14" width="7" height="7"/></svg>
                    Visão Geral
                </a>

                <a class="nav-item" onclick="switchView('gerenciar-clientes', this)">
                    <svg viewBox="0 0 24 24"><path d="M16 21v-2a4 4 0 0 0-4-4H6a4 4 0 0 0-4 4v2"/><circle cx="9" cy="7" r="4"/><path d="M22 21v-2a4 4 0 0 0-3-3.87"/><path d="M16 3.13a4 4 0 0 1 0 7.75"/></svg>
                    Gerenciar Clientes
                </a>

                <a class="nav-item" onclick="switchView('plano-fundo', this)">
                    <svg viewBox="0 0 24 24"><rect x="3" y="3" width="18" height="18" rx="2" ry="2"/><circle cx="8.5" cy="8.5" r="1.5"/><polyline points="21 15 16 10 5 21"/></svg>
                    Plano de fundo
                </a>

                <a class="nav-item" onclick="switchView('grade-apps', this)">
                    <svg viewBox="0 0 24 24"><rect x="3" y="4" width="18" height="16" rx="2"/><line x1="8" y1="4" x2="8" y2="20"/><line x1="16" y1="4" x2="16" y2="20"/></svg>
                    Grade de Apps
                </a>

                <a class="nav-item" onclick="switchView('video-publicidade', this)">
                    <svg viewBox="0 0 24 24"><polygon points="23 7 16 12 23 17 23 7"/><rect x="1" y="5" width="15" height="14" rx="2" ry="2"/></svg>
                    Video de publicidade
                </a>

                <a class="nav-item" onclick="switchView('qr-code', this)">
                    <svg viewBox="0 0 24 24"><rect x="3" y="3" width="6" height="6"/><rect x="15" y="3" width="6" height="6"/><rect x="3" y="15" width="6" height="6"/><path d="M14 14h2v2h-2z"/><path d="M18 18h2v2h-2z"/><path d="M18 14h2v2h-2z"/><path d="M14 18h2v2h-2z"/></svg>
                    QR Code WhatsApp
                </a>

                <a class="nav-item" onclick="switchView('logo-customizada', this)">
                    <svg viewBox="0 0 24 24"><path d="M4 16l4.586-4.586a2 2 0 0 1 2.828 0L16 16m-2-2l1.586-1.586a2 2 0 0 1 2.828 0L20 14m-6-6h.01M6 20h12a2 2 0 0 0 2-2V6a2 2 0 0 0-2-2H6a2 2 0 0 0-2 2v12a2 2 0 0 0 2 2z"/></svg>
                    Logo Customizada
                </a>

                <a class="nav-item" onclick="switchView('baixar-launcher', this)">
                    <svg viewBox="0 0 24 24"><polyline points="16 18 22 12 16 6"/><polyline points="8 6 2 12 8 18"/></svg>
                    Baixar Launcher
                </a>
            </nav>
        </aside>

        <!-- ÁREA PRINCIPAL -->
        <main class="main-content">
            <!-- Barra Superior -->
            <header class="top-bar">
                <div>
                    <div class="page-header-title">
                        <h1 id="topbar-title">Painel de Controle da Revenda</h1>
                        <span class="badge-user-pill"><?= htmlspecialchars($currentUser) ?></span>
                    </div>
                    <p class="page-subtitle" id="topbar-sub">Acompanhe o desempenho e a gestão dos seus clientes finais</p>
                </div>

                <div class="header-actions">
                    <button class="theme-toggle-btn" onclick="toggleTheme()" title="Alternar Modo Escuro / Claro">
                        <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M21 12.79A9 9 0 1 1 11.21 3 7 7 0 0 0 21 12.79z"/></svg>
                    </button>
                    <a href="?logout=1" class="logout-btn">Sair</a>
                </div>
            </header>

            <div class="content-body">

                <!-- 1. VISÃO GERAL (General.png) -->
                <section id="view-visao-geral" class="view-panel active">
                    <div class="content-card">
                        <div style="display:flex; justify-content:space-between; align-items:flex-start; margin-bottom:20px;">
                            <div>
                                <span class="tag-pill">PAINEL DE CONTROLE DA REVENDA</span>
                                <h2>Visão Geral: <?= htmlspecialchars($currentUser) ?></h2>
                                <p class="desc">Acompanhe abaixo o resumo dos seus dispositivos ativos, expirados e o status geral da sua revenda.</p>
                            </div>
                            <button class="btn-purple" onclick="document.querySelector('.nav-item:nth-child(2)').click();">
                                Gerenciar Meus Clientes
                            </button>
                        </div>

                        <div class="stats-grid">
                            <div class="stat-card">
                                <div class="stat-title blue">TOTAL DE DISPOSITIVOS &rarr;</div>
                                <div class="stat-number">2</div>
                                <div class="stat-desc">TV Boxes e Smart TVs cadastradas</div>
                            </div>

                            <div class="stat-card">
                                <div class="stat-title green">ASSINATURAS ATIVAS &rarr;</div>
                                <div class="stat-number">2</div>
                                <div class="stat-desc">Dispositivos com acesso liberado</div>
                            </div>

                            <div class="stat-card">
                                <div class="stat-title red">EXPIRADOS / BLOQUEADOS &rarr;</div>
                                <div class="stat-number">0</div>
                                <div class="stat-desc">Exigem renovação de plano</div>
                            </div>
                        </div>
                    </div>
                </section>

                <!-- 2. GERENCIAR CLIENTES (DEVICES.png) -->
                <section id="view-gerenciar-clientes" class="view-panel">
                    <div class="content-card">
                        <div style="display:flex; justify-content:space-between; align-items:flex-start; margin-bottom:20px;">
                            <div>
                                <span class="tag-pill">PAINEL DE CONTROLE DA REVENDA</span>
                                <h2>Clientes: <?= htmlspecialchars($currentUser) ?></h2>
                                <p class="desc">Acompanhe abaixo o resumo dos seus dispositivos ativos, expirados e o status geral da sua revenda.</p>
                            </div>
                            <button class="btn-purple" onclick="alert('Modal de cadastro de dispositivo: insira MAC e Cliente');">
                                <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><line x1="12" y1="5" x2="12" y2="19"/><line x1="5" y1="12" x2="19" y2="12"/></svg>
                                Cadastrar Novo Dispositivo
                            </button>
                        </div>

                        <!-- Barra de Pesquisa e Filtros -->
                        <div class="toolbar-row">
                            <div class="search-box">
                                <svg viewBox="0 0 24 24"><circle cx="11" cy="11" r="8"/><line x1="21" y1="21" x2="16.65" y2="16.65"/></svg>
                                <input type="text" placeholder="Buscar cliente por Nome, MAC Address ou Token...">
                            </div>

                            <div class="filter-group">
                                <button class="filter-btn active">Todos</button>
                                <button class="filter-btn">Ativos</button>
                                <button class="filter-btn">Expirados</button>
                            </div>

                            <div class="filter-group">
                                <button class="view-btn">Cards</button>
                                <button class="view-btn active">Lista</button>
                            </div>
                        </div>

                        <!-- Tabela de Dispositivos -->
                        <div class="table-responsive">
                            <table class="clients-table">
                                <thead>
                                    <tr>
                                        <th>CLIENTE / DISPOSITIVO</th>
                                        <th>MAC ADDRESS</th>
                                        <th>TOKEN DE ATIVAÇÃO</th>
                                        <th>VALIDADE</th>
                                        <th>STATUS</th>
                                        <th>AÇÕES</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <tr>
                                        <td>
                                            <div style="font-weight:700; color:var(--text-dark);">teste</div>
                                        </td>
                                        <td class="mac-col">84:D4:D2:33:D4:D8</td>
                                        <td class="token-col">84:D4:D2:33:D4:D8</td>
                                        <td><span class="date-pill">25/12/2026</span></td>
                                        <td><span class="status-pill">ATIVO</span></td>
                                        <td>
                                            <div class="actions-col">
                                                <button class="action-icon-btn edit" title="Editar"><svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M12 20h9"/><path d="M16.5 3.5a2.121 2.121 0 0 1 3 3L7 19l-4 1 1-4L16.5 3.5z"/></svg></button>
                                                <button class="action-icon-btn delete" title="Excluir"><svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><polyline points="3 6 5 6 21 6"/><path d="M19 6v14a2 2 0 0 1-2 2H7a2 2 0 0 1-2-2V6m3 0V4a2 2 0 0 1 2-2h4a2 2 0 0 1 2 2v2"/></svg></button>
                                            </div>
                                        </td>
                                    </tr>

                                    <tr>
                                        <td>
                                            <div style="font-weight:700; color:var(--text-dark);">Rene rancho</div>
                                            <div style="font-size:11px; color:#94A3B8; font-style:italic;">"Cobrar dia 10"</div>
                                        </td>
                                        <td class="mac-col">A4:B0:39:93:BC:9C</td>
                                        <td class="token-col">A4:B0:39:93:BC:9C</td>
                                        <td><span class="date-pill">25/1/2027</span></td>
                                        <td><span class="status-pill">ATIVO</span></td>
                                        <td>
                                            <div class="actions-col">
                                                <button class="action-icon-btn edit" title="Editar"><svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M12 20h9"/><path d="M16.5 3.5a2.121 2.121 0 0 1 3 3L7 19l-4 1 1-4L16.5 3.5z"/></svg></button>
                                                <button class="action-icon-btn delete" title="Excluir"><svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><polyline points="3 6 5 6 21 6"/><path d="M19 6v14a2 2 0 0 1-2 2H7a2 2 0 0 1-2-2V6m3 0V4a2 2 0 0 1 2-2h4a2 2 0 0 1 2 2v2"/></svg></button>
                                            </div>
                                        </td>
                                    </tr>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </section>

                <!-- 3. PLANO DE FUNDO -->
                <section id="view-plano-fundo" class="view-panel">
                    <div class="content-card">
                        <span class="tag-pill">CUSTOMIZAÇÃO VISUAL</span>
                        <h2>Plano de Fundo do Launcher</h2>
                        <p class="desc">Escolha as cores de fundo transmitidas para os launchers dos seus clientes.</p>

                        <div class="grid-options">
                            <div class="theme-color-pill active" style="background:#221A3B; color:#FFF;" onclick="selectThemePill(this)">Violeta Profundo</div>
                            <div class="theme-color-pill" style="background:#0F172A; color:#FFF;" onclick="selectThemePill(this)">Azul Meia-Noite</div>
                            <div class="theme-color-pill" style="background:#121214; color:#FFF;" onclick="selectThemePill(this)">Preto Ônix (OLED)</div>
                            <div class="theme-color-pill" style="background:#0A332A; color:#FFF;" onclick="selectThemePill(this)">Verde Esmeralda</div>
                            <div class="theme-color-pill" style="background:#4C3E6B; color:#FFF;" onclick="selectThemePill(this)">Lavanda Suave</div>
                            <div class="theme-color-pill" style="background:#243044; color:#FFF;" onclick="selectThemePill(this)">Cinza Ardósia</div>
                        </div>

                        <div style="margin-top:24px;">
                            <button class="btn-purple" onclick="alert('Tema de fundo transmitido para os aparelhos conectados!');">Aplicar Plano de Fundo</button>
                        </div>
                    </div>
                </section>

                <!-- 4. GRADE DE APPS -->
                <section id="view-grade-apps" class="view-panel">
                    <div class="content-card">
                        <span class="tag-pill">LAYOUT DE APLICATIVOS</span>
                        <h2>Grade de Apps da Dock Inferior</h2>
                        <p class="desc">Defina os 5 aplicativos prioritários que aparecem fixados na dock da tela inicial.</p>

                        <div style="display:flex; flex-direction:column; gap:12px; max-width:600px;">
                            <div style="display:flex; align-items:center; gap:12px; background:var(--main-bg); padding:12px 18px; border-radius:12px;">
                                <b style="color:var(--primary-purple);">App 1:</b>
                                <input type="text" class="form-control" value="UniTV (com.unitv.app)">
                            </div>
                            <div style="display:flex; align-items:center; gap:12px; background:var(--main-bg); padding:12px 18px; border-radius:12px;">
                                <b style="color:var(--primary-purple);">App 2:</b>
                                <input type="text" class="form-control" value="Google Play (com.android.vending)">
                            </div>
                            <div style="display:flex; align-items:center; gap:12px; background:var(--main-bg); padding:12px 18px; border-radius:12px;">
                                <b style="color:var(--primary-purple);">App 3:</b>
                                <input type="text" class="form-control" value="Prime Video (com.amazon.amazonvideo.livingroom)">
                            </div>
                            <div style="display:flex; align-items:center; gap:12px; background:var(--main-bg); padding:12px 18px; border-radius:12px;">
                                <b style="color:var(--primary-purple);">App 4:</b>
                                <input type="text" class="form-control" value="Netflix (com.netflix.ninja)">
                            </div>
                            <div style="display:flex; align-items:center; gap:12px; background:var(--main-bg); padding:12px 18px; border-radius:12px;">
                                <b style="color:var(--primary-purple);">App 5:</b>
                                <input type="text" class="form-control" value="YouTube TV (com.google.android.youtube.tv)">
                            </div>
                        </div>

                        <div style="margin-top:20px;">
                            <button class="btn-purple" onclick="alert('Grade de 5 apps atualizada com sucesso!');">Salvar Grade de Apps</button>
                        </div>
                    </div>
                </section>

                <!-- 5. VIDEO DE PUBLICIDADE -->
                <section id="view-video-publicidade" class="view-panel">
                    <div class="content-card">
                        <span class="tag-pill">TRANSMISSÃO EM LOOPING</span>
                        <h2>Vídeo de Publicidade</h2>
                        <p class="desc">Insira a URL do vídeo que roda continuamente na área central do launcher dos clientes.</p>

                        <div style="max-width:700px;">
                            <div class="form-group">
                                <label>URL do Vídeo (MP4 ou fluxo HLS/m3u8)</label>
                                <input type="text" class="form-control" id="videoUrlInput" value="https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4">
                            </div>
                            <button class="btn-purple" onclick="alert('URL do vídeo atualizada e sincronizada com as TVs!');">Transmitir Vídeo para Launchers</button>
                        </div>
                    </div>
                </section>

                <!-- 6. QR CODE WHATSAPP -->
                <section id="view-qr-code" class="view-panel">
                    <div class="content-card">
                        <span class="tag-pill">ATENDIMENTO AO CLIENTE</span>
                        <h2>QR Code WhatsApp de Suporte</h2>
                        <p class="desc">Gere um QR Code para que seus clientes finais possam apontar a câmera do celular na TV e abrir seu WhatsApp.</p>

                        <div style="display:flex; gap:32px; flex-wrap:wrap; margin-top:10px;">
                            <div style="flex:1; min-width:280px;">
                                <div class="form-group">
                                    <label>Seu Número de WhatsApp (com DDD)</label>
                                    <input type="text" class="form-control" value="5511987654321">
                                </div>
                                <div class="form-group">
                                    <label>Mensagem Padrão de Início</label>
                                    <input type="text" class="form-control" value="Olá, preciso de suporte com meu ATV Launcher!">
                                </div>
                                <button class="btn-purple" onclick="alert('QR Code de WhatsApp atualizado no servidor!');">Salvar QR Code</button>
                            </div>

                            <div style="width:200px; text-align:center; padding:18px; border:1px solid var(--border-light); border-radius:16px; background:var(--main-bg);">
                                <img src="https://api.qrserver.com/v1/create-qr-code/?size=160x160&data=https://wa.me/5511987654321" alt="QR Code" style="width:160px; height:160px; border-radius:8px;">
                                <p style="font-size:11px; color:var(--text-muted); margin-top:8px;">Prévia do QR Code</p>
                            </div>
                        </div>
                    </div>
                </section>

                <!-- 7. LOGO CUSTOMIZADA (LOGO.png) -->
                <section id="view-logo-customizada" class="view-panel">
                    <div class="content-card">
                        <div style="display:flex; align-items:center; gap:12px; margin-bottom:12px;">
                            <div style="width:38px; height:38px; border-radius:10px; background:#EDE9FE; display:flex; align-items:center; justify-content:center; color:#7C3AED;">
                                <svg width="22" height="22" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><rect x="3" y="3" width="18" height="18" rx="2" ry="2"/><circle cx="8.5" cy="8.5" r="1.5"/><polyline points="21 15 16 10 5 21"/></svg>
                            </div>
                            <div>
                                <h2 style="font-size:20px;">Logo Customizada</h2>
                                <p style="font-size:13px; color:var(--text-muted);">Faça o upload da logo que aparecerá no launcher dos seus clientes.</p>
                            </div>
                        </div>

                        <hr style="border:none; border-top:1px solid var(--border-light); margin:18px 0 24px 0;">

                        <div class="logo-layout-grid">
                            <!-- Coluna Esquerda: Upload -->
                            <div>
                                <label style="font-size:13px; font-weight:700; color:var(--text-dark); display:block; margin-bottom:10px;">Selecione a Imagem (PNG, JPG ou WebP)</label>
                                
                                <div class="upload-dropzone" onclick="document.getElementById('logoFileInput').click();">
                                    <input type="file" id="logoFileInput" style="display:none;" onchange="previewUploadLogo(event)">
                                    <svg viewBox="0 0 24 24"><path d="M21 15v4a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2v-4"/><polyline points="17 8 12 3 7 8"/><line x1="12" y1="3" x2="12" y2="15"/></svg>
                                    <h4>Clique para enviar <span style="font-weight:400; color:var(--text-muted);">ou arraste</span></h4>
                                    <p>Recomendado fundo transparente (PNG)</p>
                                </div>

                                <button class="btn-dark-save" onclick="alert('Logo salva e enviada com sucesso para o servidor!');">Salvar e Enviar Logo</button>
                            </div>

                            <!-- Coluna Direita: Pré-visualização -->
                            <div>
                                <div class="preview-box-card">
                                    <h5>PRÉ - VISUALIZAÇÃO</h5>
                                    
                                    <div class="preview-image-container">
                                        <div id="logoPreviewContent" style="font-size:18px; font-weight:900; letter-spacing:1px; color:#1E293B;">
                                            <span style="color:#DC2626;">KRUGER</span> VENDAS EXPRESS
                                        </div>
                                    </div>

                                    <div class="preview-badge-status">
                                        <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5"><polyline points="20 6 9 17 4 12"/></svg>
                                        Logo ativa no servidor
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </section>

                <!-- 8. BAIXAR LAUNCHER -->
                <section id="view-baixar-launcher" class="view-panel">
                    <div class="content-card">
                        <span class="tag-pill">ATUALIZAÇÃO & INSTALAÇÃO</span>
                        <h2>Baixar ATV Launcher</h2>
                        <p class="desc">Obtenha o pacote APK mais recente para instalar nas TV Boxes e Smart TVs dos seus clientes.</p>

                        <div style="display:flex; gap:28px; flex-wrap:wrap; margin-top:14px;">
                            <div style="flex:1; min-width:280px;">
                                <div style="background:var(--main-bg); padding:18px; border-radius:14px; margin-bottom:16px;">
                                    <div style="font-size:12px; color:var(--text-muted);">Versão Atual Disponível:</div>
                                    <div style="font-size:22px; font-weight:800; color:var(--text-dark);">v1.0.1 (Build 2)</div>
                                    <div style="font-size:12px; color:#10B981; margin-top:4px;">✓ Otimizado para Controle Remoto D-PAD</div>
                                </div>

                                <a href="http://177.153.68.5/painel/updates/atv-launcher-latest.apk" class="btn-purple" style="width:100%; justify-content:center;">
                                    <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M21 15v4a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2v-4"/><polyline points="7 10 12 15 17 10"/><line x1="12" y1="15" x2="12" y2="3"/></svg>
                                    Baixar Arquivo APK Direto
                                </a>
                            </div>

                            <div style="width:220px; text-align:center; padding:18px; border:1px solid var(--border-light); border-radius:16px; background:var(--main-bg);">
                                <div style="font-size:12px; font-weight:700; margin-bottom:10px;">Código Downloader TV:</div>
                                <div style="font-size:24px; font-weight:900; color:var(--primary-purple); letter-spacing:1px; background:#EDE9FE; padding:6px; border-radius:8px;">894215</div>
                                <p style="font-size:11px; color:var(--text-muted); margin-top:8px;">Digite no app Downloader da sua TV Box</p>
                            </div>
                        </div>
                    </div>
                </section>

            </div>
        </main>
    </div>

<?php endif; ?>

<script>
function switchView(viewId, element) {
    document.querySelectorAll('.view-panel').forEach(el => el.classList.remove('active'));
    document.querySelectorAll('.nav-item').forEach(el => el.classList.remove('active'));
    
    const target = document.getElementById('view-' + viewId);
    if (target) {
        target.classList.add('active');
    }
    if (element) {
        element.classList.add('active');
    }

    const titleMap = {
        'visao-geral': 'Painel de Controle da Revenda',
        'gerenciar-clientes': 'Painel de Controle da Revenda',
        'plano-fundo': 'Plano de Fundo do Launcher',
        'grade-apps': 'Grade de Aplicativos da Dock',
        'video-publicidade': 'Vídeo de Publicidade',
        'qr-code': 'QR Code WhatsApp de Suporte',
        'logo-customizada': 'Logo Customizada',
        'baixar-launcher': 'Baixar Pacote do Launcher'
    };
    if (titleMap[viewId]) {
        document.getElementById('topbar-title').innerText = titleMap[viewId];
    }
}

function toggleTheme() {
    document.body.classList.toggle('dark-theme');
    localStorage.setItem('atv_panel_theme', document.body.classList.contains('dark-theme') ? 'dark' : 'light');
}
if (localStorage.getItem('atv_panel_theme') === 'dark') {
    document.body.classList.add('dark-theme');
}

function selectThemePill(el) {
    document.querySelectorAll('.theme-color-pill').forEach(p => p.classList.remove('active'));
    el.classList.add('active');
}

function previewUploadLogo(event) {
    const file = event.target.files[0];
    if (file) {
        const reader = new FileReader();
        reader.onload = function(e) {
            document.getElementById('logoPreviewContent').innerHTML = '<img src="' + e.target.result + '" style="max-height:60px; max-width:100%; object-fit:contain;">';
        }
        reader.readAsDataURL(file);
    }
}
</script>
</body>
</html>
