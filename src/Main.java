/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Estudiante
 */
public class Main {
    public static void main(String[] args) {
        
        Parqueadero karel = new Parqueadero(5,3,75);
        karel.ingresarVehiculo("CBC101",12,20);
        karel.ingresarVehiculo("ASC121",12,26);
        
        karel.sacarVehiculo("CBC101", 12,56);
        
        karel.ingresarVehiculo("KLC121",13,0);
        karel.ingresarVehiculo("ABY001",16,0);
        karel.ingresarVehiculo("ABC121",16,2);
        karel.ingresarVehiculo("KHC121",16,12);    
        karel.ingresarVehiculo("ABM091",18,0);
        karel.ingresarVehiculo("ABC121",18,20);
        karel.ingresarVehiculo("KLC131",18,31);
        
        karel.sacarVehiculo("CBC101",18,45);
        karel.sacarVehiculo("KHC121",18,52);
        
        karel.reportarIngresos();
    }
}
