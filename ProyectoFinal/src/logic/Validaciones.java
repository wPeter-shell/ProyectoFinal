package logic;

public class Validaciones {

   public static boolean tieneNumero(String cadena) {
      if (cadena == null) return false;
      return cadena.matches(".*\\d.*");
   }

   public static boolean tieneLetra(String cadena) {
      if (cadena == null) return false;
      return cadena.matches(".*[a-zA-ZáéíóúÁÉÍÓÚñÑ].*");
   }

   public static boolean soloLetrasEspanol(String cadena) {
      if (cadena == null) return false;
      return cadena.matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+");
   }

   public static boolean soloNumeros(String cadena) {
      if (cadena == null) return false;
      return cadena.matches("\\d+");
   }
}
