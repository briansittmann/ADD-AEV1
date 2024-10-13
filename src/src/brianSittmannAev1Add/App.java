package src.brianSittmannAev1Add;
import java.io.File;
import java.util.Scanner;

/**
 * Clase principal de la aplicación. Permite gestionar directorios y archivos mediante un menu interactivo.
 * Se pueden realizar acciones como listar archivos, buscar cadenas de texto y reemplazar cadenas en archivos.
 */
public class App {
	
    /**
     * Metodo principal que ejecuta la aplicación.
     * Permite al usuario interactuar con la aplicación a través de un menu.
     * 
     * @param args  Si se proporciona un directorio como argumento de programa, sera usado como el directorio de trabajo inicial.
     * sino se pedira uno por teclado.
     */
	public static void main(String[] args) {
		Scanner teclado = new Scanner(System.in);
		GestorArchivos gestorArchivos = new GestorArchivos();
		String directorio = null;
		boolean continuar = true;
		
		// Comprueba si se ha pasado un directorio como argumento.
		if(args.length > 0 ) {
			directorio = args[0];
			if(!esDirectorioValido(directorio)) {
				System.out.println("El directorio proporcionado no es valido.");
				directorio = null;
			}
		}
		
		// Solicitar un directorio valido si no se proporciono o es invalido.
		while (directorio == null || !esDirectorioValido(directorio)) {
			System.out.printf("Introduzca un directorio: ");
			directorio = teclado.nextLine();
			if(!esDirectorioValido(directorio)) {
				System.out.println("El directorio no es valido, intentelo otra vez. ");
			}	
		}
		
		System.out.println("Directorio: '" + directorio + "' es valido. ");
		
		// Bucle principal para mostrar el menu y procesar las opciones del usuario.
		while(continuar) {
			mostrarMenu();
			String opcion = comprobarOpcion(teclado);
			
			switch(opcion) {
				case "1":
					gestorArchivos.listarArchivos(directorio);
					break;
				case "2":
					System.out.print("Introduce la cadena a buscar: ");
					String cadena = teclado.nextLine();
					System.out.println("¿Quiere Ignorar las mayusculas? (S/N): ");
					boolean ignorarMayusculas = teclado.nextLine().equalsIgnoreCase("S");
					System.out.println("¿Quiere Ignorar los acentos? (S/N): ");
					boolean ignorarAcentos = teclado.nextLine().equalsIgnoreCase("S");
					gestorArchivos.buscarCadenaEnArchivos(directorio, cadena, ignorarMayusculas, ignorarAcentos);
					break;
				case "3":
					System.out.print("Introduce la cadena a remplazar: ");
					String cadenaOriginal = teclado.nextLine();
					System.out.print("Introduce la nueva cadena: ");
					String cadenaNueva = teclado.nextLine();
					System.out.println("¿Quiere Ignorar las mayusculas? (S/N): ");
					ignorarMayusculas = teclado.nextLine().equalsIgnoreCase("S");
					System.out.println("¿Quiere Ignorar los acentos? (S/N): ");
					ignorarAcentos = teclado.nextLine().equalsIgnoreCase("S");
					gestorArchivos.reemplazarCadenaEnArchivos(directorio, cadenaOriginal, cadenaNueva, ignorarMayusculas, ignorarAcentos);
					break;
				case "4":
					System.out.print("introcuzca un nuevo directorio: ");
					directorio = teclado.nextLine();
					if(esDirectorioValido(directorio)) {
						System.out.println("El directorio se ha cambiado con exito.");
					}else {
						System.out.println("El directorio no es valido, se mantiene el anterior");
					}
					break;
				case "0":
					System.out.println("Saliendo...");
					System.out.println("Hasta luego!");
					continuar = false;
					break;
				default:
					System.out.println("Opcion no valida, intentelo otra vez.");
					
			}
		}
		
		teclado.close();
	}
	
    /**
     * Muestra el menu de opciones disponibles.
     */
    public static void mostrarMenu() {
        System.out.println("\nMenu de opciones: ");
        System.out.println("1. Listar archivos del directorio actual");
        System.out.println("2. Buscar cadena en archivos");
        System.out.println("3. Reemplazar cadena en archivos");
        System.out.println("4. Cambiar de directorio");
        System.out.println("0. Salir");
        System.out.print("Elige una opción: ");
    }
    
    /**
     * Comprueba si el directorio proporcionado es valido.
     * 
     * @param directorio Ruta del directorio a comprobar.
     * @return true si es valido (si existe y es un directorio), false en caso contrario.
     */
    public static boolean esDirectorioValido(String directorio) {
    	File file = new File(directorio);
    	return file.exists() && file.isDirectory();
    }
    
    /**
     * Comprueba que la opción seleccionada por el usuario es valida.
     * 
     * @param teclado Scanner para leer la entrada del usuario.
     * @return Opción valida seleccionada.
     */
    public static String comprobarOpcion(Scanner teclado) {
    	String opcion;
    	boolean esValida = false;
    	do {
    		opcion = teclado.nextLine();
    		if(opcion.equals("0") || opcion.equals("1") || opcion.equals("2") || opcion.equals("3") || opcion.equals("4")) {
                esValida = true;
    		}else {
    			System.out.println("Opcion no valida! Introduzca una opcion entre 0 y 4");
    		}
    	}while(!esValida);
    	return opcion;
   
    }
    
}
