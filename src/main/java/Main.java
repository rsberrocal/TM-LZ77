import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;

public class Main {
    static int MDES = 0;
    static int MENT = 0;
    static String filenameInput = "data.txt";

    public static void readFile() {
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(filenameInput));
            String line = reader.readLine();
            while (line != null) {
                String[] data = line.split(",");
                if (data[0].equals("Ment")) {
                    int ment = Integer.parseInt(data[1]);
                    if (ment % 2 != 0) {
                        System.out.println("Parametro Ment incorrecto!");
                    } else {
                        MENT = ment;
                    }
                } else if (data[0].equals("Mdes")) {
                    int mdes = Integer.parseInt(data[1]);
                    if (mdes % 2 != 0) {
                        System.out.println("Parametro Ment incorrecto!");
                    } else {
                        MDES = mdes;
                    }
                } else {
                    System.out.println("Parametro incorrecto!");
                }
                // read next line
                line = reader.readLine();
            }

            if (MENT == 0 || MDES == 0) {
                System.out.println("Faltan parametros!");
                System.exit(0);
            }

            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static int parseBit(String bits, int aux){
        int res = 0;
        //Si no existe ningun 1, significa que son todos 0
        if (!bits.contains("1")){
            return aux;
        }
        for(int i = bits.length() - 1; i >= 0; i--){
            if (bits.charAt(i) == '1'){
                res += Math.pow(2, bits.length() - i);
            }
        }
        return res;
    }

    public static String parseInt(int n, int bits){
        StringBuilder res = new StringBuilder();
        String b = Integer.toBinaryString(n);
        if (b.length() < bits){
            for (int i = b.length(); i < bits; i++){
                res.append("0");
            }
            res.append(b);
        }else {
            res.append(b,b.length() - bits,b.length());
        }
        return res.toString();
    }

    public static String compress(String input) {
        int idxDes = 0; // La ventana deslizante empieza en el 0
        int idxEnt = MDES; // La ventana de entrada empieza donde acaba la ventana deslizante
        StringBuilder res = new StringBuilder();
        res.append(input, 0, MDES);// Empezamos la compresion con la ventana deslizante
        res.append(",");
        // Mientras no hayamos llegado al final
        int l = input.length();
        while ((idxEnt + MENT) <= input.length()) {
            // Vamos compromiendo
            // Cogemos las ventanas del input encontrado
            String wDes = input.substring(idxDes, idxDes + MDES);
            String wEnt = input.substring(idxEnt, idxEnt + MENT);
            boolean hasMatch = false;
            int aux = MENT;

            // Miramos si en la ventana de entrada tenemos un match
            while (aux > 1 && !hasMatch) {
                // Miramos si la cadena actual de la ventana de entrada existe dentro de la ventana deslizante
                String actual = wEnt.substring(0, aux);
                if (wDes.contains(wEnt.substring(0, aux))) {
                    // Match
                    int D = MDES - wDes.lastIndexOf(wEnt.substring(0, aux)); // Recogemos la distancia
                    int L = aux; // Recogemos la longitud del match encontrado
                    // Lo añadimos al resultado
                    res.append("(" + parseInt(L,MENT) + "," + parseInt(D, MDES) + ")");
                    //Miramos si estamos en la parte final y nos queda algo restante
                    if ((idxEnt + MENT) == input.length() && L != MENT){
                        // Añadimos lo que quede
                        res.append(input,idxEnt + L, input.length());
                    }

                    // Marcamos match
                    hasMatch = true;
                    // Posicionamos las ventanas
                    idxDes += L;
                    idxEnt += L;
                }
                aux--;
            }

            if (!hasMatch) {
                // Si no se ha encontrado ningun resultado ponemos el primer bit de la ventana de entrada
                res.append(wEnt.charAt(0));
                // Sumamos 1 a las ventanas
                idxDes++;
                idxEnt++;
            }
        }
        return res.toString();
    }

    public static String unCompress(String input) {
        String res = "";
        for(int i = 0; i<input.length();i++){  //pasa por t odo el input
            boolean modeCompact = false;        //modeCompact es para indicar que se tiene que hacer el protocolo de union
            String longi="", despl = "";

            if(input.charAt(i) == '('){  //indica al finalizar i iniciar del protocolo
                modeCompact = true;
            }else if(input.charAt(i) == ')'){
                modeCompact = false;
            }
            if(!modeCompact && (input.charAt(i) == '0' || input.charAt(i) == '1')){
                res += input.charAt(i);     //en caso que el protocolo no este activo se añade los bits
            }
            if(modeCompact){        //protocolo activo
                longi = input.substring(i+1,i+7);       //sacamos el valor de los numeros entre los parentesis
                int nlongi = Integer.parseInt(longi, 2);
                despl = input.substring(i+9, i+16);
                int ndespl =Integer.parseInt(despl, 2);

                String inc = "";
                int compte = 0;
                while(compte != nlongi){
                    inc += res.charAt(res.length()-ndespl+compte); //va recorriendo el string añadiendo de izquierda a derecha
                    //hasta que compte sea igual a la distancia que queriamos utilizar.
                    compte += 1;
                }
                res += inc;  //incrementamos al resultado
                i += 15;  //saltamos los parentesis
            }
        }
        return res;
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        readFile();
        Random rand = new Random();
        int random = rand.nextInt(33554432);
        String numRandom = parseInt(random, 25);

        if (MENT > MDES) {
            System.out.println("Error en la configuracion.");
            System.out.println("La ventana de entrada es mayor a la deslizante");
            System.exit(0);
        }
        // PILLAR ENTRADA
        String input = "11011100101001111010100010001";
        String compression = compress(input);

        System.out.println("Numero: "+ input);
        System.out.println("Coding: " + compression);
        System.out.println("Uncode: " + unCompress(compression));

        System.out.println("Numero aleatori de 25 bits: "+ numRandom);
        String randomCompression = compress(numRandom);
        System.out.println("Coding numero aleatori: "+ randomCompression);
        System.out.println("Uncoding numero aleatori: "+ unCompress(randomCompression));

        if (MENT + MDES > input.length()) {
            System.out.println("Error en la configuracion.");
            System.out.println("La ventana total es mayor al input");
            System.exit(0);
        }


    }

}
