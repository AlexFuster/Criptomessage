package view;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
public class GeneralViewController {
	public static final int BLOCKL=8;
	public static final File ARCHIVOGRANDE = new File("Resources/contenedor.txt");
	String inText, outText, kText;
	private MainApp mainApp;
	int contadorFallos;
	@FXML
	private TextArea tin;
	@FXML
	private TextArea tout;
	@FXML
	private TextField kWord;
	@FXML
	
	private void handleEncode(){
		try{
		inText = tin.getText();
		kText = kWord.getText();
		outText = codificar(kText.trim()+"a",inText.trim());
		if(outText.length()>8000){
			try{
				PrintWriter pw = new PrintWriter(ARCHIVOGRANDE);
				pw.print(outText);
				pw.close();
				
				System.out.println("aqui2");
			}catch(IOException ioe){ioe.printStackTrace();}
			tout.setText("La codificación es demasiado grande para mostrarla en la interfaz.\nEl mensaje codificado se encuentra en un archivo de texto llamado contenedor.txt");
		}
		//else
			tout.setText(outText);
		}catch(Exception e){
			tout.setText("Encoding error.");
		}
	}
	@FXML
	private void handleDecode(){
		try{
		inText = tin.getText();
		kText = kWord.getText();
		outText = decodificar(kText.trim()+"a",inText.trim());		
		tout.setText(outText);
		}catch(Exception e){
			tout.setText("Decoding error.");
		}
		if(contadorFallos==0)mainApp.initDesbloqueo();
	}
	@FXML
	private void handleFlecha(){
		tin.setText(tout.getText());
	}
	@FXML
	private void handleLimpiar(){
		tin.setText("");
	}
	@FXML
	private void handleCopiar(){
		tout.selectAll();
		tout.copy();
	}
	public String  codificar(String palabra, String mensaje){
		if(mensaje.equals(""))return "";
		char[] blocksMes = mensaje.toCharArray();
		char[] blocksKey = palabra.toCharArray();
		String result="";
		int count=0;
		for(int i=0;i<blocksMes.length;i++){
			int ant=0;
			for(int j=0;j<blocksKey.length;j++){
				ant=k(ant^(int)blocksMes[i],(int)blocksKey[j]);
				result+= (char)(ant+256);
				if((i!=(blocksMes.length-1)||j!=(blocksKey.length-1))){
					if(count!=0&& count%12==0){
						result+="\n";
						count=-1;
					}
				}
				count++;
			}
		}
		System.out.println(result.length());
		return result;
	}
	public String  decodificar(String palabra, String mensaje){
		int[] blocksMes = toIntArray2(mensaje);
		char[] blocksKey = palabra.toCharArray();
		char[] auxAr = new char[blocksMes.length/blocksKey.length];
		String result="";
		for(int i=0;i<auxAr.length;i++){
			int ant=0;
			for(int j=0;j<blocksKey.length;j++){
					int aux2=reverseK(blocksMes[j+i*blocksKey.length],(int)blocksKey[j])^ant;
					ant=blocksMes[j+i*blocksKey.length];
					if(j==0)auxAr[i]=(char)aux2;
					else if((int)auxAr[i]!=aux2){
							try{
								ObjectOutputStream ous = new ObjectOutputStream(new FileOutputStream(MainApp.UNLOCKFILE));
								ous.writeInt(--contadorFallos);
								ous.close();
							}catch(IOException ioe){}
						return "Wrong word.\nYou have "+contadorFallos+" more tries";
					}
			}
			result += auxAr[i];
		}
		try{
			contadorFallos=3;
			ObjectOutputStream ous = new ObjectOutputStream(new FileOutputStream(MainApp.UNLOCKFILE));
			ous.writeInt(contadorFallos);
			ous.close();
		}catch(IOException ioe){}
		return result;
	}
	private int k(int b1, int k1){
		int result = b1+k1;
		/*if(result>Math.pow(2,BLOCKL)-1)result -= Math.pow(2, BLOCKL);*/
		return result;
	}
	private int reverseK(int r1,int k1){
		int result = r1-k1;
		/*if(r1<k1)result += Math.pow(2,BLOCKL);*/
		return result;
	}
	public int[] toIntArray2(String str){
		str=str.replaceAll("\n", "");
		char[] ch = str.toCharArray();
		int[] result = new int[ch.length];
		for(int i=0;i<result.length;i++)result[i]=(int)ch[i] - 256;
		return result;
	}
	public void setMainApp(MainApp m){
		mainApp=m;
	}
	public void setContadorFallos(int n){
		contadorFallos=n;
	}
}
