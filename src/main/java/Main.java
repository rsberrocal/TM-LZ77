import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;

import javax.sound.midi.Soundbank;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;

public class Main {
    static int MDES = 0;
    static int MENT = 0;
    static String filenameInput = "data.txt";

    public static boolean isValid(int x)
    {
        return (x != 0) && ((x & (x - 1)) == 0);
    }


    public static String readDataFromFile(String file){
        return lz77.TxtReader.cargarTxt(file).toString();
    }

    public static String getAscii(String data){
        StringBuffer stringBuffer = new StringBuffer(data);
        return lz77.TxtReader.ASCIIbin2string(stringBuffer).toString();
    }

    public static void readFile() {
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(filenameInput));
            String line = reader.readLine();
            while (line != null) {
                String[] data = line.split(",");
                if (data[0].equals("Ment")) {
                    int ment = Integer.parseInt(data[1]);
                    if (!isValid(ment)) {
                        System.out.println("Parametro Ment incorrecto!");
                    } else {
                        MENT = ment;
                    }
                } else if (data[0].equals("Mdes")) {
                    int mdes = Integer.parseInt(data[1]);
                    if (!isValid(mdes)) {
                        System.out.println("Parametro Mdes incorrecto!");
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

    public static int log2(int N)
    {

        // calculate log2 N indirectly
        // using log() method
        int result = (int)(Math.log(N) / Math.log(2));

        return result;
    }

    public static int parseBit(String bits, int aux){
        int res = 0;
        //Si no existe ningun 1, significa que son todos 0
        if (!bits.contains("1")){
            return aux;
        }
        for(int i = bits.length() - 1; i >= 0; i--){
            if (bits.charAt(i) == '1'){
                int exp = bits.length() - 1 - i;
                res += Math.pow(2, exp);
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
            res.append(b,b.length() - bits, b.length());
        }
        return res.toString();
    }

    public static String compress(String input, boolean needFormat) {
        System.out.println("Comprimiendo...");
        long startTime = System.nanoTime();

        int idxDes = 0; // La ventana deslizante empieza en el 0
        int idxEnt = MDES; // La ventana de entrada empieza donde acaba la ventana deslizante
        StringBuilder res = new StringBuilder();
        res.append(input, 0, MDES);// Empezamos la compresion con la ventana deslizante
        if (needFormat){
            res.append(",");
        }
        // Mientras no hayamos llegado al final
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
                String c = wEnt.substring(0, aux);
                if (wDes.contains(wEnt.substring(0, aux))) {
                    // Match
                    int D = MDES - wDes.lastIndexOf(wEnt.substring(0, aux)); // Recogemos la distancia
                    int L = aux; // Recogemos la longitud del match encontrado
                    // Lo añadimos al resultado
                    //res.append("(" + parseInt(L,log2(MENT)) + "," + parseInt(D, log2(MENT)) + ")");
                    if (needFormat){
                        res.append("(" + parseInt(L,log2(MENT)) + "," + parseInt(D, log2(MDES)) + ")");
                    } else {
                        res.append(parseInt(L,log2(MENT)) + parseInt(D, log2(MDES)));
                    }
                    //Miramos si estamos en la parte final y nos queda algo restante
                    if ((idxEnt + MENT) == input.length() && L != MENT){
                        // Añadimos lo que quede
                        res.append(input,idxEnt + L, input.length());
                        L = MENT;
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
        // Miramos el restante
        if (idxEnt != input.length()){
            res.append(input, idxEnt, input.length());
        }
        long stopTime = System.nanoTime();
        System.out.println("Datos comprimidos.");
        System.out.println("Tiempo: " + (stopTime - startTime) + " ns");
        return res.toString();
    }

    public static String unCompress(String input) {
        StringBuilder res = new StringBuilder();
        for(int i = 0; i<input.length();i++){  //pasa por t odo el input
            boolean modeCompact = false;        //modeCompact es para indicar que se tiene que hacer el protocolo de union
            String longi="", despl = "";
            if(input.charAt(i) == '('){  //indica al finalizar i iniciar del protocolo
                modeCompact = true;
            }else if(input.charAt(i) == ')'){
                modeCompact = false;
            }
            if(!modeCompact && (input.charAt(i) == '0' || input.charAt(i) == '1')){
                res.append(input.charAt(i));     //en caso que el protocolo no este activo se añade los bits
            }
            char x = input.charAt(i);
            if(modeCompact){        //protocolo activo
                longi = input.substring(i + 1, i + 1 + log2(MENT));       //sacamos el valor de los numeros entre los parentesis
                int nlongi = parseBit(longi, MENT);
                despl = input.substring(i + 2 + log2(MENT), i + 2 + log2(MENT) + log2(MDES));
                int ndespl = parseBit(despl, MDES);

                String a = res.substring((res.length()) -ndespl, (res.length()) -ndespl + nlongi);
                res.append(a);
                i = i + 2 + log2(MENT) + log2(MDES);
            }
        }
        return res.toString();
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        readFile();
        if (MENT > MDES) {
            System.out.println("Error en la configuracion.");
            System.out.println("La ventana de entrada es mayor a la deslizante");
            System.exit(0);
        }
        String data = readDataFromFile("text/quijote_short.txt");
        String compression = compress(data, true);

        String compressNoFormat = compress(data, false);// Se comprime otra vez para obtener el resultado sin el formato de ,(L,D),
        double compressRatio = (double) data.length() / compressNoFormat.length();
        System.out.println("Compress ratio: " + compressRatio);

        String des = unCompress(compression);
        if (!des.equals(data)){
            System.out.println("Error al descomprimir");
        }

        /*System.out.println(des);
        System.out.println(getAscii(des));

        Random rand = new Random();
        int random = rand.nextInt(33554432);
        String numRandom = parseInt(random, log2(33554432));


        // PILLAR ENTRADA
        //String input = "11011100101001111010100010001";
        String input = "1100000001000001111011000"; // probar
        String compression = compress(input);
        int y =log2(MENT);
        int x =log2(MDES);
        System.out.println("Numero: "+ input);
        System.out.println("Coding: " + compression);
        System.out.println("Uncode: " + unCompress(compression));

        System.out.println("Numero aleatori de 25 bits: "+ numRandom);
        String randomCompression = compress(numRandom);
        System.out.println("Coding numero aleatori: "+ randomCompression);
        System.out.println("Uncoding numero aleatori: "+ unCompress(randomCompression));
        */


    }

}
