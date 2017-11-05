<!DOCTYPE html>
<html lang="es">
<head>

  <title>Practica 4 </title>
  <meta charset="utf-8">
	<link rel="import" href="./miscomponentes/encabezado-cuestionario.html">
  <link href="https://fonts.googleapis.com/css?family=Droid+Serif" rel="stylesheet">
  <link href="https://fonts.googleapis.com/css?family=Ubuntu" rel="stylesheet">
  <link id="estilo" rel="stylesheet" type="text/css" href="./css/normal.css">
  <script src="./js/prueba.js"></script>
<script src="./webcomponentsjs/webcomponents-lite.js"></script>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/2.1.1/jquery.min.js"></script>
</head>
<body onload="init()">
  <header>
<h1>q�estiona</h1>
  <p>
    q�estiona es una aplicaci�n web que permite gestionar cuestionarios sobre un determinado tema en el que las posibles respuestas son verdadero o falso. La aplicaci�n se desarrolla como parte de las pr�cticas de la asignatura Desarrollo de Aplicaciones en Internet del Grado en Ingenier�a Inform�tica de la Universitat d'Alacant.  
  </p>
    <nav>
      <ul>
	<li><a href="#par�s">Par�s</a></li>
	<li><a href="#londres">Londres</a> </li>
      </ul>
    </nav>
  </header>
  <main>
   <div class="formulario" id="nuevoCuestionario">
        <ul>
           <li>
               <label>Tema del cuestionario:</label>
               <input type="text" name="tema" autofocus>
           </li>
           <li>
               <input type="button" name="crea" value="Crear nuevo cuestionario" onclick="addCuestionario(this)">
           </li>
        </ul>
    </div>
  </main>
  <footer>
    <span id="nombre">Jorge Prieto Martin</span> |
    <span id="dni">48625766V</span> |
    <span id="usuario"><%= request.getAttribute("usuario")%></span> |
    <span id="valor"><a href= <%= request.getAttribute("logout")%>>Salir</a></span> |
  </footer>
</body>
</html>