/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Estudiante
 */
import becker.robots.*;
import java.awt.Color;

public class Parqueadero{
    private City ciudad;
    public Robot robot1;
    private int[] ocupados; //Arreglo que guarda cuantos puestos ocupados tiene cada seccion
    private int secciones; //Numero de secciones
    private int puestos; //Numero de puestos
    private String[][] usuarios; //Matriz que guarda las placas de los usi¿uarios en el puesto donde fue guardado su vehiculo
    private int[][] horaIngreso; //Matriz que guarda la hora de ingreso de un vehiculo según su puesto en el parqueadero
    private int ingresos; //Ingresos totales
    private int tarifa; //Tarifa por minuto
    
// Parqueadero(Numero de puestos de cada seccion, Numero de seccciones,Precio por minuto en pesos colombianos)
    public Parqueadero(int p, int s,int tarifa) {
       this.secciones=s;
       this.puestos=p;
       this.ocupados= new int[s];
       this.usuarios= new String[s][p]; 
       this.horaIngreso = new int[s][p];
       this.tarifa=tarifa;
       this.ciudad = new City("Parqueadero.txt");
       
       //Todas las secciones tienen 0 espacios ocupados
       for(int i=0;i<s;i++) this.ocupados[i]=0;
       
       //Armando el parqueadero segun los parametros de secciones y puestos
        for (int i = 1; i <= s+1; i++) {
            Wall pared = new Wall(ciudad, 1, i, Direction.NORTH); 
            pared = new Wall(ciudad, p+1, i, Direction.SOUTH);
            for(int j=1; j<=p;j++){
                pared = new Wall(ciudad, j, i, Direction.WEST);
            }            
        }        
        for(int i=1;i<=p-1;i++){
            Wall pared = new Wall(ciudad,i,s+1,Direction.EAST);
        }
        Wall pared = new Wall(ciudad,p+1,s+1,Direction.EAST);  
        this.robot1 = new Robot(ciudad, p, s+2, Direction.WEST);       
        
        this.robot1.setColor(Color.ORANGE);
        
        
    }
    
    //Ingreso de un vehiculo con una placa a una hora del dia dada
    public void ingresarVehiculo(String placa,int hora,int minuto){
        
        //El vehivulo a estacionar será un Thing llevado por el robot
        Thing carro= new Thing(ciudad,this.puestos,this.secciones+2); 
        carro.getIcon().setColor(Color.LIGHT_GRAY);
        carro.getIcon().setLabel(placa);
        
        
        int seccion=this.seccionMasVacia(); //Establece cual es la seccion mas vacia
        
        this.robot1.pickThing();
        
        this.robot1.move();
        this.robot1.turnLeft();
        this.robot1.move();
        for(int i=0;i<3;i++)this.robot1.turnLeft();
        
        for(int i=1;i<=seccion+1;i++){ //Se mueve a la seccion más vacía y más cercana 
            this.robot1.move();
        }
        
        //Se mueve al ultimo puesto vacío y deja el vehiculo ahí
        for(int i=0;i<3;i++)this.robot1.turnLeft();        
        for(int i=this.ocupados[seccion];i<this.puestos;i++){
            this.robot1.move();
        }        
        this.robot1.putThing();
        
        //El robot vuelve a la posicion inicial donde recibe los vehivulos
        for(int i=0;i<2;i++)this.robot1.turnLeft();        
        for(int i=this.ocupados[seccion];i<this.puestos;i++){
            this.robot1.move();
        }        
        this.robot1.turnLeft();        
        for(int i=1;i<=seccion+1;i++)this.robot1.move();        
        this.robot1.turnLeft();
        this.robot1.move();
        for(int i=0;i<3;i++)this.robot1.turnLeft();
        this.robot1.move();
        for(int i=0;i<2;i++)this.robot1.turnLeft();
        
        
        /*Se guarda la placa y la hora de ingreso en una posicion de la matriz que representa
        el parqueadero, para conocer su posicion mas adelante, y el precio a pagar*/
        this.usuarios[seccion][this.ocupados[seccion]]=placa;         
        this.horaIngreso[seccion][this.ocupados[seccion]]=hora*60+minuto;
                
        this.ocupados[seccion]++; //Ha sido ocupado un puesto en esta seccion
        
        
        
    }
    public void sacarVehiculo(String placa, int hora, int minuto){
        int s = 0,p = 0;
        
        //Ciclo que busca la ubicacion del vehiculo con la placa dada
        for(int i=0;i<this.secciones;i++){
            for(int j=0;j<this.ocupados[i];j++){
                if(this.usuarios[i][j].equals(placa)){
                    s=i;
                    p=j;
                }
            }
        }
        
        //Se mueve el robot a la seccipon del vehiculo a sacar        
        this.robot1.move();
        this.robot1.turnLeft();
        this.robot1.move();
        for(int i=0;i<3;i++)this.robot1.turnLeft();        
        for(int i=1;i<=s+1;i++)this.robot1.move();        
        for(int i=0;i<3;i++)this.robot1.turnLeft();
        
        
        
        for(int i=0;i<this.ocupados[s]-p;i++){//En caso de tener que sacar algun vehiculo que bloquea al que se quiere sacar definitivamente a la zona temporal
            for(int j=0;j<=this.puestos-this.ocupados[s]+i;j++)this.robot1.move();
            this.robot1.pickThing();
            for(int j=0;j<2;j++)this.robot1.turnLeft();            
            for(int j=0;j<=this.puestos-this.ocupados[s]+i;j++)this.robot1.move();
            
            if(i+1==this.ocupados[s]-p){//Si ya se sacaron todos los obstaculos para llegar al carro que se quiere sacar
                for(int j=0;j<3;j++)this.robot1.turnLeft();
                for(int j=1;j<=this.secciones-s;j++)this.robot1.move();
                this.robot1.putThing();
            }else{ //saca un vehiculo que bloquea al de atras y lo introduce en la zona temporal
                this.robot1.turnLeft();
                for(int j=1;j<=s+1;j++)this.robot1.move();
                this.robot1.turnLeft();


                for(int j=1;j<=this.puestos-i;j++)this.robot1.move();
                this.robot1.putThing();

                for(int j=0;j<2;j++)this.robot1.turnLeft();
                for(int j=1;j<=this.puestos-i;j++)this.robot1.move();

                for(int j=0;j<3;j++)this.robot1.turnLeft();

                for(int j=1;j<=s+1;j++)this.robot1.move();
                for(int j=0;j<3;j++)this.robot1.turnLeft();
            }                        
        }
        
        //Saca al carro que sale del parqueadero definitivamente
        for(int j=0;j<2;j++)this.robot1.turnLeft();
        for(int j=0;j<=this.secciones;j++)this.robot1.move();
        this.robot1.turnLeft();
        
        //Este ciclo hace que el robot vuelva a meter los vehiculos que estaban en la zona temporal si es el caso
        for(int i=0;i<this.ocupados[s]-p-1;i++){
            for(int j=0;j<=this.puestos-this.ocupados[s]+p+1+i;j++)this.robot1.move();
            this.robot1.pickThing();
            for(int j=0;j<2;j++)this.robot1.turnLeft();
            for(int j=0;j<=this.puestos-this.ocupados[s]+p+i+1;j++)this.robot1.move();
            for(int j=0;j<3;j++)this.robot1.turnLeft();
            for(int j=1;j<=s+1;j++)this.robot1.move();
            for(int j=0;j<3;j++)this.robot1.turnLeft();
            for(int j=0;j<this.puestos-p-i;j++)this.robot1.move();
            this.robot1.putThing();
            for(int j=0;j<2;j++)this.robot1.turnLeft();
            for(int j=0;j<this.puestos-p-i;j++)this.robot1.move();
            this.robot1.turnLeft();
            for(int j=1;j<=s+1;j++)this.robot1.move();
            this.robot1.turnLeft();
            
        }
        
        //El robot vuelve a la posicion inicial 
        this.robot1.move();
        for(int j=0;j<3;j++)this.robot1.turnLeft();
        this.robot1.move();
        for(int j=0;j<2;j++)this.robot1.turnLeft();
        
        //Se reorganiza la matriz de placas pues los vehiculos cambiaron de puestos
        for(int i=p;i<this.ocupados[s]-1;i++){
            this.usuarios[s][i]=this.usuarios[s][i+1];
        }
        
        this.ingresos+=this.tarifa*(hora*60+minuto-this.horaIngreso[s][p]);//Se cobra al usi¿uario del parqueadero según su tiempo
        
        this.robot1.setLabel(String.valueOf(this.ingresos));//El robot muestra sus ingresos al momento
        
        this.ocupados[s]--;//Se ha desocupado un puesto de esta seccion
    }
    
    public int seccionMasVacia(){
        int sec=0;
        for(int i=1;i<this.secciones;i++){
            if(this.ocupados[sec]>this.ocupados[i]){
                sec=i;
            }
        }
        
        return sec;
        
    }
    
    //Imprime en consola los ingresos hasta el momento
    public void reportarIngresos(){
        System.out.println("Se generaron $"+String.valueOf(this.ingresos)+" en esta jornada.");
    }
            
    
    
    
}
