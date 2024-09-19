/*
* Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
*/
package escuelaing.edu.co.calcreflex;
 
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Objects;
 

public class CalcReflexBEServer {
 
    public static void main(String[] args) throws IOException, URISyntaxException {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(36000);
        } catch (IOException e) {
            System.err.println("Could not listen on port: 35000.");
            System.exit(1);
        }
 
        boolean running = true;
        while (running) {
            Socket clientSocket = null;
            try {
                System.out.println("Listo para recibir ...");
                clientSocket = serverSocket.accept();
            } catch (IOException e) {
                System.err.println("Accept failed.");
                System.exit(1);
            }
            PrintWriter out = new PrintWriter(
                    clientSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(clientSocket.getInputStream()));
            String inputLine, outputLine;
            boolean isFirstLine = true;
            String firstLine = "";
            while ((inputLine = in.readLine()) != null) {
                System.out.println("Recibí: " + inputLine);
                if (isFirstLine) {
                    firstLine = inputLine;
                    isFirstLine = false;
                }
                if (!in.ready()) {
                    break;
                }
            }
 
            URI requestURL = getRequestURI(firstLine);
 
            if (requestURL.getPath().startsWith("/compreflex")) {
                outputLine = "HTTP/1.1 200 OK\r\n"
                        + "Content-Type: application/json\r\n"
                        + "\r\n"
                        + "{\"name\":\"John\", \"age\":30, \"car\":null}";
            } else {
                outputLine = getDefaultResponse();
            }
 
            out.println(outputLine);
            out.close();
            in.close();
            clientSocket.close();
        }
        serverSocket.close();
 
    }

    public static String getDefaultResponse() {
        String htmlcode
                = "HTTP/1.1 200 OK\r\n"
                + "Content-Type: text/html\r\n"
                + "\r\n"
                + "<!DOCTYPE html>\n"
                + "<html>\n"
                + "    <head>\n"
                + "        <title>Form Example</title>\n"
                + "        <meta charset=\"UTF-8\">\n"
                + "        <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n"
                + "    </head>\n"
                + "    <body>\n"
                + "        <h1>Calculate</h1>\n"
                + "        <form onsubmit=\"return false;\">\n"
                + "            <label for=\"compute\">compute:</label><br>\n"
                + "            <input type=\"text\" id=\"compute\" name=\"compute\"><br>\n"
                + "            <label for=\"params\">Parameters (comma separated):</label><br>\n"
                + "            <input type=\"text\" id=\"params\" name=\"params\"><br><br>\n"
                + "            <input type=\"button\" value=\"Submit\" onclick=\"computeResult()\">\n"
                + "        </form> \n"
                + "        <div id=\"result\"></div>\n"
                + "\n"
                + "        <script>\n"
                + "            function compute() {\n"
                + "                let command = document.getElementById(\"compute\").value;\n"
                + "                let params = document.getElementById(\"params\").value.split(',');\n"
                + "                const xhttp = new XMLHttpRequest();\n"
                + "                xhttp.onload = function() {\n"
                + "                    const response = JSON.parse(this.responseText);\n"
                + "                    document.getElementById(\"result\").innerHTML = 'Result: ' + response.result;\n"
                + "                }\n"
                + "                xhttp.open(\"GET\", \"/computar?command=\" + compute + \"&params=\" + params.join(','));\n"
                + "                xhttp.send();\n"
                + "            }\n"
                + "        </script>\n"
                + "\n"
                + "    </body>\n"
                + "</html>";
        return htmlcode;
    }


    public static URI getRequestURI(String firstline) throws URISyntaxException {
 
        String ruri = firstline.split(" ")[1];
 
        return new URI(ruri);
 
    }
 
    
    public static String computeMathCommand(String command) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException{
        Class c = Math.class;
        Class [] parametersTypes = {double.class};
        Method m = c.getDeclaredMethod("abs", parametersTypes);
        Object[] params = {-2,0};
        String resp = m.invoke(null, (Object) params).toString();
        return "";
    }

    public String compute(String command, Double[] params) {
        try {
            if ("bbl".equalsIgnoreCase(command)) {
                bubbleSort(params);
                return Arrays.toString(params);
            } else {
                Method method = Math.class.getMethod(command, Double.TYPE);
                Double result = (Double) method.invoke(null, params[0]);
                return result.toString();
            }
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    private void bubbleSort(Double[] array) {
        boolean sorted;
        do {
            sorted = true;
            for (int i = 0; i < array.length - 1; i++) {
                if (array[i] > array[i + 1]) {
                    Double temp = array[i];
                    array[i] = array[i + 1];
                    array[i + 1] = temp;
                    sorted = false;
                }
            }
        } while (!sorted);
    }
}

