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