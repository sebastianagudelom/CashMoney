package co.edu.uniquindio.managers;

import co.edu.uniquindio.models.Cliente;
import co.edu.uniquindio.models.Monedero;

public class GestorCanjes {

    private static final int PUNTOS_BONO_10 = 500;
    private static final int PUNTOS_BONO_50 = 1000;
    private static final int PUNTOS_BONO_150 = 2000;

    private static final int BONO_10 = 10;
    private static final int BONO_50 = 50;
    private static final int BONO_150 = 150;

    public static boolean canjearBonoMonedero(Cliente cliente, int puntos, Monedero monedero) {
        int bono = 0;

        if (puntos >= PUNTOS_BONO_150) {
            bono = BONO_150;
            puntos = PUNTOS_BONO_150;
        } else if (puntos >= PUNTOS_BONO_50) {
            bono = BONO_50;
            puntos = PUNTOS_BONO_50;
        } else if (puntos >= PUNTOS_BONO_10) {
            bono = BONO_10;
            puntos = PUNTOS_BONO_10;
        } else {
            return false;
        }

        if (monedero != null) {
            monedero.setSaldo(monedero.getSaldo() + bono);
            GestorClientes.getSistemaPuntos().restarPuntos(cliente.getIdentificacion(), puntos);
            return true;
        }

        return false;
    }
}
