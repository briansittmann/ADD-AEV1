package src.brianSittmannAev1Add;

import java.text.Normalizer;

/**
 * Clase de utilidades para realizar las operaciones de procesamiento de texto.
 * Incluye funcionalidades para normalizar texto, ajustar mayúsculas y eliminar acentos.
 */
public class UtilidadesTexto {

    /**
     * Normaliza el texto eliminando los acentos.
     * 
     * @param texto Texto a normalizar.
     * @param eliminarAcentos Indica si se deben eliminar los acentos.
     * @return Texto normalizado.
     */
    public static String normalizarTexto(String texto, boolean eliminarAcentos) {
        if (eliminarAcentos) {
            texto = Normalizer.normalize(texto, Normalizer.Form.NFD);
            texto = texto.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");
        }
        return texto;
    }
    
    /**
     * Ajusta el texto a minusculas si se indica.
     * 
     * @param texto Texto a ajustar.
     * @param ignorarMayusculas Indica si se deben ignorar las mayusculas.
     * @return Texto ajustado.
     */
    public static String ajustarMayusculas(String texto, boolean ignorarMayusculas) {
        if (ignorarMayusculas) {
            return texto.toLowerCase();
        }
        return texto;
    }
    
    /**
     * Procesa el texto normalizandolo(sin acentos) y ajustando las mayusculas segun los parametros proporcionados.
     * 
     * @param texto Texto a procesar.
     * @param ignorarMayusculas Indica si se deben ignorar las mayusculas.
     * @param ignorarAcentos Indica si se deben ignorar los acentos.
     * @return Texto procesado.
     */
    public static String procesarTexto(String texto, boolean ignorarMayusculas, boolean ignorarAcentos) {
        texto = normalizarTexto(texto, ignorarAcentos);
        texto = ajustarMayusculas(texto, ignorarMayusculas);
        return texto;
    }
}
