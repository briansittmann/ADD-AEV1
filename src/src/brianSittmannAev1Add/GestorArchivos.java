package src.brianSittmannAev1Add;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Clase que gestiona operaciones de archivos y directorios.
 * Permite listar archivos en formato de arbol, buscar cadenas en archivos de texto plano y reemplazar cadenas.
 */
public class GestorArchivos {
	
    /**
     * Lista todos los archivos y directorios del directorio proporcionado, mostrando una estructura en forma de arbol.
     * 
     * @param directorio Ruta del directorio a listar.
     */
    public void listarArchivos(String directorio) {
        File dir = new File(directorio);
        if (dir.exists() && dir.isDirectory()) {
            File[] archivos = dir.listFiles();
            if (archivos != null) {
                List<File> directorios = new ArrayList<>();
                List<File> archivosEnRaiz = new ArrayList<>();
                
                // Clasifica archivos y directorios
                for (File archivo : archivos) {
                    if (archivo.isDirectory() && !archivo.isHidden() && archivo.canRead()) {
                        directorios.add(archivo);
                    } else if (archivo.isFile() && !archivo.isHidden() && archivo.canRead()) {
                        archivosEnRaiz.add(archivo);
                    }
                }

                // Listar directorios 
                for (File subdir : directorios) {
                	// Recursion para listar directorios
                	listarContenidoEnFormatoArbol(subdir, 0);  
                }

                // Listar archivos en la raiz
                for (File archivoRaiz : archivosEnRaiz) {
                    System.out.println("|-- " + obtenerInformacionArchivo(archivoRaiz));
                }
            } else {
                System.out.println("El directorio no se puede leer o esta vacio.");
            }
        } else {
            System.out.println("El directorio no existe.");
        }
    }

    
    /**
     * Metodo recursivo para listar contenido en formato de arbol.
     * 
     * @param dir Directorio a listar.
     * @param nivel Nivel de profundidad para la indentacion del arbol.
     */
    public void listarContenidoEnFormatoArbol(File dir, int nivel) {
        File[] archivos = dir.listFiles();
        if (archivos != null && archivos.length > 0) {
            List<File> directorios = new ArrayList<>();
            List<File> archivosEnDirectorio = new ArrayList<>();

            // Separar archivos y directorios
            for (File archivo : archivos) {
                if (archivo.isDirectory() && !archivo.isHidden() && archivo.canRead()) {
                    directorios.add(archivo);
                } else if (archivo.isFile() && !archivo.isHidden() && archivo.canRead()) {
                    archivosEnDirectorio.add(archivo);
                }
            }

            // Listar subdirectorios recursivamente
            for (File subdir : directorios) {
                imprimirIndentacion(nivel);
                System.out.println("|-- " + subdir.getName());
                listarContenidoEnFormatoArbol(subdir, nivel + 1);  
            }

            // Listar archivos en el directorio actual
            for (File archivo : archivosEnDirectorio) {
                imprimirIndentacion(nivel + 1);
                System.out.println("|-- " + obtenerInformacionArchivo(archivo));
            }
        }
    }
   

    /**
     * Obtiene información basica de un archivo, como el nombre, tamaño y ultima modificación.
     * 
     * @param archivo Archivo del que se va a obtener la información.
     * @return Información del archivo en formato de String.
     */
    public String obtenerInformacionArchivo(File archivo) {
        return archivo.getName() + " (" + String.format("%.2f", archivo.length() / 1024.0) + " KB - " + new Date(archivo.lastModified()) + ")";
    }

    
    /**
     * Imprime la cantidad de indentación adecuada para mostrar la estructura en arbol.
     * 
     * @param nivel Nivel de profundidad del arbol.
     */
    private void imprimirIndentacion(int nivel) {
        for (int i = 0; i < nivel; i++) {
            System.out.print("|   ");
        }
    }

    
    /**
     * Busca una cadena de texto en los archivos de texto plano del directorio.
     * 
     * @param directorio Ruta del directorio donde se realizara la busqueda.
     * @param cadena La cadena de texto a buscar.
     * @param ignorarMayusculas Si se deben ignorar las mayusculas durante la busqueda.
     * @param ignorarAcentos Si se deben ignorar los acentos durante la busqueda.
     */
    public void buscarCadenaEnArchivos(String directorio, String cadena, boolean ignorarMayusculas, boolean ignorarAcentos) {
        File dir = new File(directorio);
        if (dir.exists() && dir.isDirectory()) {
            String cadenaProcesada = UtilidadesTexto.procesarTexto(cadena, ignorarMayusculas, ignorarAcentos);
            buscarCadenaImprimirCoincidencias(dir, cadenaProcesada, ignorarMayusculas, ignorarAcentos, 0);  // Inicia búsqueda desde nivel 0
        } else {
            System.out.println("El directorio no es válido.");
        }
    }
    
    
    /**
     * Busca una cadena en todos los archivos de texto plano en un directorio de manera recursiva y imprime sus coincidencias.
     * 
     * @param dir Directorio en el que se realizarA la bUsqueda.
     * @param cadena Cadena a buscar.
     * @param ignorarMayusculas Si se deben ignorar las mayusculas durante la busqueda.
     * @param ignorarAcentos Si se deben ignorar los acentos durante la busqueda.
     * @param nivel Nivel de profundidad para la indentación del arbol.
     */
    public void buscarCadenaImprimirCoincidencias(File dir, String cadena, boolean ignorarMayusculas, boolean ignorarAcentos, int nivel) {
        File[] archivos = dir.listFiles();
        if (archivos != null && archivos.length > 0) {
            List<File> directorios = new ArrayList<>();
            List<File> archivosEnDirectorio = new ArrayList<>();

            // Separar archivos y directorios
            for (File archivo : archivos) {
                if (archivo.isDirectory()) {
                    directorios.add(archivo);
                } else if (esArchivoTextoPlano(archivo)) {
                    archivosEnDirectorio.add(archivo);
                }
            }

            // Primero, listar los subdirectorios de manera recursiva
            for (File subdir : directorios) {
                imprimirIndentacion(nivel);
                System.out.println("|-- " + subdir.getName());
                buscarCadenaImprimirCoincidencias(subdir, cadena, ignorarMayusculas, ignorarAcentos, nivel + 1);  // Recursión
            }

            // Después, listar los archivos en el directorio actual con el número de coincidencias
            for (File archivo : archivosEnDirectorio) {
                int coincidencias = buscarCadenaEnArchivo(archivo, cadena, ignorarMayusculas, ignorarAcentos);
                imprimirIndentacion(nivel + 1);
                System.out.println("|-- " + archivo.getName() + " (" + coincidencias + " coincidencias)");
            }

            // Listar archivos no accesibles (por ejemplo, archivos Word, PDF, etc.)
            for (File archivo : archivos) {
                if (!esArchivoTextoPlano(archivo)) {
                    imprimirIndentacion(nivel + 1);
                    System.out.println("|-- " + archivo.getName() + " (0 coincidencias - archivo no accesible)");
                }
            }
        }
    }
    
    
    /**
     * Busca cuantas veces aparece una cadena en un archivo de texto(coincidencias).
     * 
     * @param archivo Archivo en el que se realizará la búsqueda.
     * @param cadena Cadena a buscar.
     * @param ignorarMayusculas Si se deben ignorar las mayúsculas durante la búsqueda.
     * @param ignorarAcentos Si se deben ignorar los acentos durante la búsqueda.
     * @return Numero de veces que aparece la cadena en el archivo.
     */
    public int buscarCadenaEnArchivo(File archivo, String cadena, boolean ignorarMayusculas, boolean ignorarAcentos) {
        int coincidencias = 0;

        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String cadenaProcesada = UtilidadesTexto.procesarTexto(cadena, ignorarMayusculas, ignorarAcentos);
            String linea;

            while ((linea = br.readLine()) != null) {
                String lineaProcesada = UtilidadesTexto.procesarTexto(linea, ignorarMayusculas, ignorarAcentos);
                int indice = lineaProcesada.indexOf(cadenaProcesada);

                // Contamos cuantas veces aparece la cadena en la linea
                while (indice != -1) {
                    coincidencias++;
                    indice = lineaProcesada.indexOf(cadenaProcesada, indice + 1);
                }
            }
        } catch (IOException e) {
            System.out.println("No se pudo leer el archivo: " + archivo.getName());
        }

        return coincidencias;
    }

    
    /**
     * Reemplaza una cadena por otra en todos los archivos de texto plano de un directorio.
     * 
     * @param directorio Ruta del directorio donde se realizara el reemplazo.
     * @param cadenaOriginal La cadena original a reemplazar.
     * @param cadenaNueva La nueva cadena que reemplazara a la original.
     * @param ignorarMayusculas Si se deben ignorar las mayusculas durante el reemplazo.
     * @param ignorarAcentos Si se deben ignorar los acentos durante el reemplazo.
     */
    public void reemplazarCadenaEnArchivos(String directorio, String cadenaOriginal, String cadenaNueva, boolean ignorarMayusculas, boolean ignorarAcentos) {
        File dir = new File(directorio);
        if (dir.exists() && dir.isDirectory()) {
            File[] archivos = dir.listFiles();
            if (archivos != null) {
                String cadenaOriginalProcesada = UtilidadesTexto.procesarTexto(cadenaOriginal, ignorarMayusculas, ignorarAcentos);
                
                // Reemplazar la cadena en cada archivo de texto plano
                for (File archivo : archivos) {
                    if (esArchivoTextoPlano(archivo)) {
                        int reemplazos = reemplazarCadenaEnArchivo(archivo, cadenaOriginalProcesada, cadenaNueva, ignorarMayusculas, ignorarAcentos);
                        System.out.println(archivo.getName() + " (" + reemplazos + " reemplazos)");
                    } else {
                        System.out.println(archivo.getName() + " (0 reemplazos - archivo no accesible)");
                    }
                }
            }
        }
    }

    /**
     * Reemplaza una cadena por otra en un archivo de texto plano especifico.
     * 
     * @param archivo Archivo en el que se realizara el reemplazo.
     * @param cadenaOriginal La cadena original a reemplazar.
     * @param cadenaNueva La nueva cadena que reemplazara a la original.
     * @param ignorarMayusculas Si se deben ignorar las mayusculas durante el reemplazo.
     * @param ignorarAcentos Si se deben ignorar los acentos durante el reemplazo.
     * @return Numero de veces que la cadena fue reemplazada en el archivo.
     */
    public int reemplazarCadenaEnArchivo(File archivo, String cadenaOriginal, String cadenaNueva, boolean ignorarMayusculas, boolean ignorarAcentos) {
        int reemplazos = 0;
        File archivoModificado = new File(archivo.getParent(), "MOD_" + archivo.getName());

        try (BufferedReader br = new BufferedReader(new FileReader(archivo));
             BufferedWriter bw = new BufferedWriter(new FileWriter(archivoModificado))) {

            String linea;
            while ((linea = br.readLine()) != null) {
                String lineaProcesada = UtilidadesTexto.procesarTexto(linea, ignorarMayusculas, ignorarAcentos);
                int indice = lineaProcesada.indexOf(cadenaOriginal);
                
                // Contar y realizar los reemplazos
                while (indice != -1) {
                    reemplazos++;
                    indice = lineaProcesada.indexOf(cadenaOriginal, indice + 1);
                }

                // Escribir la linea modificada en el archivo nuevo
                String lineaModificada = lineaProcesada.replace(cadenaOriginal, cadenaNueva);
                bw.write(lineaModificada);
                bw.newLine();
            }

            System.out.println("Archivo modificado guardado como: " + archivoModificado.getName());

        } catch (IOException e) {
            System.out.println("No se pudo modificar el archivo: " + archivo.getName() + " - " + e.getMessage());
        }

        return reemplazos;
    }

    
    /**
     * Verifica si un archivo es de texto plano basado en su extensión.
     * Se va a considerar como archivos de texto plano aquellos con extensiones como .txt, .java, .md, .html, y .log.
     * 
     * @param archivo Archivo a verificar.
     * @return true si es un archivo de texto plano, false en caso contrario.
     */
    public boolean esArchivoTextoPlano(File archivo) {
        String nombreArchivo = archivo.getName().toLowerCase();
        return nombreArchivo.endsWith(".txt") || 
               nombreArchivo.endsWith(".java") || 
               nombreArchivo.endsWith(".md") || 
               nombreArchivo.endsWith(".html") || 
               nombreArchivo.endsWith(".log");
    }

}
