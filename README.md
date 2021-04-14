# TM-LZ77
Miembros del grupo:
    - Richard Sudario Berrocal
    - Nil Viñals Nin

Para modificar los valores de ventana de entrada y ventana deslizante se ha de modificar el fichero data.txt
Siguiendo este formato:
Ment,#
Mdes,#


Para ejectuarlo se puede usar el siguiente comando
java -jar target/TM-LZ77-LZ77.jar

Preguntas a responder:

2.Comprimid el archivo “hamlet_short.txt” con distintos valores de Mdes y Ment entre 4 y 4096 y analizad el factor de compresión y el tiempo invertido para conseguirlo. ¿Cuál es el mejor factor de compresión que obtenéis y con qué valores de Mdes y Ment? ¿Cómo varia (cómo escala) el tiempo de cálculo necesario al aumentar Mdes y Ment? ¿Qué combinación de Mdes y Ment elegiríais?

![Grafiques](https://user-images.githubusercontent.com/31955883/114478041-b0318a80-9bfd-11eb-8309-5f0df6048fe4.png)
En estas imágenes se puede observar una serie de resultados según el 2^MDES, 2^MENT y el tiempo en segundos. Donde podemos ver que sale una aproximación de solo 2 decimales, por ejemplo la opción A, no es que ha tardado 0 segundos, sino que ha tardado inferior a 0,00s, pero podemos ver la diferencia entre los demás igualmente, se puede ver que al hacer las ventanas más grandes más tarda, según lo que hemos visto beneficia tener un MENT pequeño pero no muy diferente a MDES. 



3.Comprimid ahora el archivo “quijote_short.txt” y analizad para qué combinación de Mdes y Ment se obtiene el mejor factor de compresión. ¿Es el mismo que en el caso anterior? Proponed varias razones que expliquen esta diferencia.

| MDES        | MENT           | ratio  |
| ----------- |:--------------:| ------:|
|	8	|	4	|	0.5965270684371808	|
|	8	|	8	|	0.5368351936966513	|
|	16	|	8	|	0.5791598781610824	|
|	32	|	16	|	0.617056603773585	|
|	64	|	16	|	0.7175706512199403	|
|	256	|	128	|	0.742327946250227	|
|	1024	|	512	|	0.9074361820199778	|
|	4096	|	2048	|	0.9941634241245136	|


Viendo los resultados se ve que con ventanas mas grandes el resultado tiende mas a 1. A pesar de obtener un mejor resultado su tiempo de ejecucion es mayor, debido a que las comparaciones que se hacen son mayores por la ventana de entrada.

Con ventanas mas pequeñas el ratio es menor debido a que el resultado comprimido es mayor que al original
