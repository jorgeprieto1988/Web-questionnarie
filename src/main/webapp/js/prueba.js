var autoid = 0;

function insertAsFirstChild(padre,hijo)
{
	if(!padre.hasChildNodes())
	{
		padre.appendChild(hijo);
	}
	else
	{
		padre.insertBefore(hijo,padre.childNodes[0]);
	}
	
}

function insertAfterChild(padre,hijo,nuevoHijo)
{
	if(padre.childNodes.length > 1)
	{
		padre.insertBefore(nuevoHijo,hijo.nextSibling);
	}
	else
	{
		padre.appendChild(nuevoHijo);
	}	
}


function addCruz(node) 
{
	var borrar = document.createElement("div");
	borrar.classList.add("borra");
	borrar.textContent = "☒";
	borrar.onclick = borraPregunta;
	insertAsFirstChild(node,borrar);
} 

 function queryAncestorSelector (node,selector) 
 {
      var parent= node.parentNode;
      var all = document.querySelectorAll(selector);
      var found= false;
      while (parent !== document && !found) {
        for (var i = 0; i < all.length && !found; i++) {
          found= (all[i] === parent)?true:false;
        }
        parent= (!found)?parent.parentNode:parent;
      }
      return (found)?parent:null;
}
	
	
function borraPregunta() //hay que pasar parámetro?
{
	var bloque = queryAncestorSelector(this,"div.bloque");
	var cuestionario = bloque.parentNode;
	var tema = cuestionario.getElementsByTagName("encabezado-cuestionario")[0].tema;	
	var pregunta = bloque.getElementsByClassName("pregunta")[0];
	$.getJSON('borrapregunta', {"tema": tema, "pregunta": pregunta.textContent}, 
			function(data)
			{
				if(data.hasOwnProperty('result'))
				{
					bloque.parentNode.removeChild(bloque);
				}
				else
				{
					window.alert("Error " + data.error.message);
				}
			});
	
	
	if(cuestionario.querySelector("div.bloque")== null)
	{		
	$.getJSON('borracuestionario', {"tema": tema}, 
			function(data)
			{
				if(data.hasOwnProperty('result'))
				{
					var cuestid = "#" + cuestionario.id;
					cuestionario.parentNode.removeChild(cuestionario);
					var listalinks = document.querySelectorAll("a");
					
					var encontrado = false;
					var i = 0;
					var link;
					while(encontrado == false && i < listalinks.length)
					{
						if(listalinks[i].hash == cuestid)
						{
							link = listalinks[i];
							encontrado = true;
						}
						else
						{
							i++;
						}
						
					}
					
					var listali = queryAncestorSelector(link,"li");
					listali.parentNode.removeChild(listali);
				}				
				else
				{
					window.alert("Error " + data.error.message);
				}
			});	
	}
} 

function addBloque(cuestionario, textpregunta, datavalor)
{
	var bloque = document.createElement("div");
	bloque.classList.add("bloque");
	
	var pregunta= document.createElement("div");
	pregunta.classList.add("pregunta");
	pregunta.textContent = textpregunta;
	//pregunta.innerHTML = pregunta + textpregunta;
	
	var respuesta = document.createElement("div");
	respuesta.classList.add("respuesta");
	respuesta.setAttribute("data-valor",datavalor);
	//respuesta.datavalor = datavalor;
	
	bloque.appendChild(pregunta);
	bloque.appendChild(respuesta);
	addCruz(bloque);
	cuestionario.appendChild(bloque);
}

function addPregunta() 
{
	var formulario = queryAncestorSelector(this,"div.formulario");
	var name_id = formulario.parentNode.id;
	var pregunta = document.getElementsByName(formulario.parentNode.id + "_pregunta")[0];
	var bloque = formulario.parentNode;
	var cuestionario = formulario.parentNode;
	var tema = cuestionario.getElementsByTagName("encabezado-cuestionario")[0].tema;
	var respuesta = "true";
	if (!formulario.getElementsByTagName("input")[1].checked)
		{
		respuesta = "false";
		}
	
	if (pregunta.value != "")
	{
		
		$.getJSON('nuevapregunta', {"pregunta": pregunta.value, "respuesta": respuesta, "tema": tema},
				function(data)
				{
					if(data.hasOwnProperty('result'))
					{
						addBloque(bloque, pregunta.value, respuesta);
						formulario.getElementsByTagName("input")[1].checked = true;
						pregunta.value = "";
					}				
					else
					{
						window.alert("Error " + data.error.message);
					}
				});
								
	}
	else
	{
		window.alert("No puedes dejar ningún campo sin rellenar");
	}
}

function addIndex(tema,nameid)
{
	var index = document.getElementsByTagName("nav")[0];
	
	var ul = index.childNodes[1];
	var li = document.createElement("li");
	var a = document.createElement("a");
	
	a.href = "#" + nameid;
	a.textContent = tema;
	
	li.appendChild(a);
	ul.appendChild(li);
}

function addSection(main,temasec, url)
{
	autoid ++;
	var id = "c" + autoid;
	
	var section = document.createElement("section");
	section.id = id;
	section.innerHTML = "<encabezado-cuestionario tema=" + temasec+ "></encabezado-cuestionario>";
	addFormPregunta(section);
	main.appendChild(section);
	addIndex(temasec, id);
	
}

function addCuestionario(element)
{
	var formulario = queryAncestorSelector(element,"div.formulario");
	var tema = formulario.getElementsByTagName("input")[0];
	/*
	if(tema.value != "")
	{
		addSection(formulario.parentNode,tema.value);
		tema.value="";
	}
	else
	{
		window.alert("No puedes dejar ningún campo sin rellenar");
	}
	*/
	if(tema.value != "")
	{
		$.getJSON('nuevocuestionario', {"tema": tema.value}, 
				function(data)
				{
					if(data.hasOwnProperty('result'))
					{
						addSection(formulario.parentNode,tema.value);
						tema.value="";
					}				
					else
					{
						window.alert("Error " + data.error.message);
					}
				});
	}
	else
	{
		window.alert("No puedes dejar ningún campo sin rellenar");
	}
	
}

function LoadPregunta(tema)
{
	var temas = document.getElementsByTagName("encabezado-cuestionario");
	
	for(i=0; i < temas.length; i++)
	{
		if(temas[i].getAttribute("tema")  == tema)
		{
			bloque = temas[i].parentNode;
			console.log(bloque);
		}
	}
	
	$.getJSON('listapreguntas', {"tema": tema}).then( 
			function(data)
			{
				console.log(data);
				if(data.hasOwnProperty('result'))
				{
					$.each(data.result, function(key) {
						console.log(data.result[key].pregunta);
						console.log(data.result[key].respuesta);
						addBloque(bloque, data.result[key].pregunta, data.result[key].respuesta);
					});
				}				
			});

}

function LoadCuestionario()
{
	
	$.getJSON('listacuestionarios', 
			function(data)
			{
				console.log(data);
				if(data.hasOwnProperty('result'))
				{
					$.each(data.result, function(key) {
						addSection(document.getElementsByTagName("main")[0], data.result[key]);
						LoadPregunta(data.result[key]);
					});
				}				
			});
			
}

function init()
{
	LoadCuestionario();
	
	var bloques = document.getElementsByClassName("bloque");
	var size = bloques.length;
	for (var i = 0; i < size; i++) 
	{
		addCruz(bloques[i]);
	}
	
	var sections = document.getElementsByTagName("section");
	var size = sections.length;
	for (var i = 0; i < size; i++) 
	{	
		addFormPregunta(sections[i]);
	}

}

 function addFormPregunta(section)
 {	
	var formulario = document.createElement("div");
	formulario.classList.add("formulario");
	
	var list = document.createElement("ul");
	var firstli = document.createElement("li");
	
	var labelenunciado = document.createElement("label");
	labelenunciado.textContent = "Enunciado de la pregunta:";
	
	var inputpregunta = document.createElement("input");
	inputpregunta.type = "text";
	inputpregunta.name = section.id + "_pregunta";
	
	firstli.appendChild(labelenunciado);
	firstli.appendChild(inputpregunta);
	
	var secondli = document.createElement("li");
	
	var labelrespuesta = document.createElement("label");
	labelrespuesta.textContent = "Respuesta:";
	
	var labelverdadero = document.createElement("span");
	labelverdadero.textContent = "Verdadero";
	
	var labelfalso = document.createElement("span");
	labelfalso.textContent = "Falso";
	
	var inputtrue = document.createElement("input");
	inputtrue.type = "radio";
	inputtrue.name = section.id + "_respuesta";
	inputtrue.value = "verdadero";
	inputtrue.checked = true;
	//inputtrue.textContent = "Verdadero";
	
	var inputfalse = document.createElement("input");
	inputfalse.type = "radio";
	inputfalse.name = section.id + "_respuesta";
	inputfalse.value = "falso";
	//inputfalse.textContent = "Falso";
	
	secondli.appendChild(labelrespuesta);
	secondli.appendChild(inputtrue);
	secondli.appendChild(labelverdadero);
	secondli.appendChild(inputfalse);
	secondli.appendChild(labelfalso);
	
	var thirdli = document.createElement("li");
	
	var boton = document.createElement("input");
	boton.type = "button";
	boton.value = "Añadir nueva pregunta";
	boton.onclick = addPregunta;
	
	thirdli.appendChild(boton);
	
	list.appendChild(firstli);
	list.appendChild(secondli);
	list.appendChild(thirdli);
	
	formulario.appendChild(list);
	
	var bloque = section.getElementsByClassName("bloque")[0];
	if(bloque != null)
	{
		section.insertBefore(formulario,bloque);
	}
	else
	{
		section.appendChild(formulario);
	}
	
	
	//var titulo = section.getElementsByTagName("h2")[0];
	//insertAfterChild(section,titulo,formulario);
 }


