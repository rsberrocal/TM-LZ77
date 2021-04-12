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

3.Comprimid ahora el archivo “quijote_short.txt” y analizad para qué combinación de Mdes y Ment se obtiene el mejor factor de compresión. ¿Es el mismo que en el caso anterior? Proponed varias razones que expliquen esta diferencia.