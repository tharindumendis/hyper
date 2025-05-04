<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Hyper POS - Home</title>
    <link rel="stylesheet" href="styles.css"> <!-- External CSS -->
</head>
<body>
<div class="container">
    <header>
        <h1>Hyper POS</h1>
        <nav>
            <ul>
                <li><a href="#">Dashboard</a></li>
                <li><a href="#">Inventory</a></li>
                <li><a href="#">Sales</a></li>
                <li><a href="#">Settings</a></li>
            </ul>
        </nav>
    </header>

    <main>
        <section class="hero">
            <h2>Efficient & Modern POS Solution</h2>
            <p>Manage your inventory and sales seamlessly with Hyper POS.</p>
            <button onclick="showAlert()">Get Started</button>
        </section>
    </main>

    <footer>
        <p>© 2025 Hyper POS. All rights reserved.</p>
    </footer>
</div>

<script>
    function showAlert() {
        alert('Welcome to Hyper POS!');
    }
</script>
</body>
</html>