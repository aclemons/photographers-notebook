package unisiegen.photographers.export;

import java.lang.reflect.Field;
import java.util.ArrayList;

import android.util.Log;


public class XMLExport {
	
	/**
	 * Eigener XML Parser, geht in die Tiefe bis n = 2
	 * Objekte und Variablen müssen public sein !
	 */
	
	public XMLExport () {}
	
	public String toXML (Object object) {
		String XMLString = "<?xml version=1.0 encoding=UTF-8?> \n <Film>";
		Field[] masterClassField = object.getClass().getFields();
		for(int y = 0 ; y < masterClassField.length ; y++) {
			Log.v("XML","<" + masterClassField[y].getName() + ">");
			XMLString = XMLString + "\n" + "   <" + masterClassField[y].getName() + ">";
			try {
				if(masterClassField[y].get(object) instanceof ArrayList<?>) {
					ArrayList<?> Liste = (ArrayList<?>) masterClassField[y].get(object);
					for(int h = 0 ; h < Liste.size() ; h++) {
						Field[] masterClassFieldEbeneOne = Liste.get(h).getClass().getFields();
						Log.v("XML","   <" + Liste.get(h).getClass().getSimpleName() + "> \n");
						XMLString = XMLString + "\n" + "      <" + Liste.get(h).getClass().getSimpleName() + ">";
						for(int f = 0 ; f < masterClassFieldEbeneOne.length ; f++) {
							if(masterClassFieldEbeneOne[f].get(Liste.get(h)) instanceof ArrayList<?>) {
								Log.v("XML","      </" + masterClassFieldEbeneOne[f].getName() + "> \n");
								XMLString = XMLString + "\n" + "         </" + masterClassFieldEbeneOne[f].getName() + ">";
								ArrayList<?> Listen = (ArrayList<?>) masterClassFieldEbeneOne[f].get(Liste.get(h));
								for(int o = 0 ; o < Listen.size() ; o++) {
									Field[] masterClassFieldEbeneTwo = Listen.get(o).getClass().getFields();
									Log.v("XML","         <" + Listen.get(o).getClass().getSimpleName() + "> \n");
									XMLString = XMLString + "\n" + "            <" + Listen.get(o).getClass().getSimpleName() + ">";
									for(int s = 0 ; s < masterClassFieldEbeneTwo.length ; s++) {
										if(masterClassFieldEbeneTwo[s].get(Listen.get(o)) instanceof ArrayList<?>) {
											
										} else {
											Log.v("XML","            <" + masterClassFieldEbeneTwo[s].getName() + ">"+masterClassFieldEbeneTwo[s].get(Listen.get(o))+"</" + masterClassFieldEbeneTwo[s].getName() + ">");
											XMLString = XMLString + "\n" + "               <" + masterClassFieldEbeneTwo[s].getName() + ">"+masterClassFieldEbeneTwo[s].get(Listen.get(o))+"</" + masterClassFieldEbeneTwo[s].getName() + ">";
										}
									}
									Log.v("XML","         </" + Listen.get(o).getClass().getSimpleName() + "> \n");
									XMLString = XMLString + "\n" + "            </" + Listen.get(o).getClass().getSimpleName() + ">";
								}
								Log.v("XML","      </" + masterClassFieldEbeneOne[f].getName() + "> \n");
								XMLString = XMLString + "\n" + "         </" + masterClassFieldEbeneOne[f].getName() + ">";
							} else {
								Log.v("XML","      <" + masterClassFieldEbeneOne[f].getName() + ">"+masterClassFieldEbeneOne[f].get(Liste.get(h))+"</" + masterClassFieldEbeneOne[f].getName() + ">");
								XMLString = XMLString + "\n" + "         <" + masterClassFieldEbeneOne[f].getName() + ">"+masterClassFieldEbeneOne[f].get(Liste.get(h))+"</" + masterClassFieldEbeneOne[f].getName() + ">";
							}
						}
						Log.v("XML","   </" + Liste.get(h).getClass().getSimpleName() + "> \n");
						XMLString = XMLString + "\n" + "      </" + Liste.get(h).getClass().getSimpleName() + ">";
					}
					Log.v("XML","</" + masterClassField[y].getName() + "> \n");
					XMLString = XMLString + "\n" + "   </" + masterClassField[y].getName() + ">";
				} else {
					Field[] fieldone = masterClassField[y].get(object).getClass().getFields();
					for(int z = 0 ; z < fieldone.length ; z++) {
						Log.v("XML","   <" + fieldone[z].getName() + ">"+fieldone[z].get(masterClassField[y].get(object))+"</" + fieldone[z].getName() + ">");
						XMLString = XMLString + "\n" + "      <" + fieldone[z].getName() + ">"+fieldone[z].get(masterClassField[y].get(object))+"</" + fieldone[z].getName() + ">";
						Log.v("Check","Variable : " + fieldone[z].getName() + " Typ ("+fieldone[z].getType()+")");
						Log.v("Check","    ->   Wert : " + fieldone[z].get(masterClassField[y].get(object)));
					}
					Log.v("XML","</" + masterClassField[y].getName() + "> \n");
					XMLString = XMLString + "\n" + "   </" + masterClassField[y].getName() + ">";
				}
			} catch (Exception e) {
				 e.printStackTrace();
				 Log.v("XML","Error : "+e);
			}
		}
		
		XMLString = XMLString + "\n" + "</Film>";
		return XMLString;
	}
	
	
}