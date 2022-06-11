package de.hs_kl.staab.playground;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * This class is to try out things and play around with the REST-Api. Feel free
 * to edit it! :)
 */
@RestController
@RequestMapping(path = "/sandbox")
public class RestSandbox {

	/**
	 * BEISPIEL-GET-METHODE: Mit einer GET-Methode kann man im Browser sich
	 * Informationen anzeigen lassen, die die Anwendung bereitstellt.
	 * 
	 * Um diese Methode aufzurufen, einfach die Anwendung starten (MainApplication)
	 * und dann im Browser die folgende URL eingeben:
	 * http://localhost:8080/sandbox/example
	 * 
	 * Man kann sich das auch in Swagger anzeigen lassen, dazu diese URL verwenden:
	 * http://localhost:8080/swagger-ui/
	 * 
	 * @return The Text "Hello World"
	 */
	@RequestMapping(value = "/example", method = RequestMethod.GET)
	public String helloWorldExample() {
		return "Hello World!";
	}

	/**
	 * BEISPIEL-GET-METHODE: Ein weiteres GET-Beispiel: Diese GET-Methode gibt den
	 * Inhalt einer ganzen Klasse zurück:
	 * http://localhost:8080/sandbox/example2?name="Slim Shady"
	 *
	 * Ein GET-Request kann wie in diesem Fall auch einen Parameter bekommen, sollte
	 * aber niemals Inhalte in der Anwendung ändern (also nur "Read-Only"-Zugriffe).
	 * 
	 * Beispielanwendungsfall: Sie wollen sich die Einträge des Kalenders an einem
	 * bestimmten Tag anzeigen lassen.
	 * 
	 * @return The content of a class
	 */
	@RequestMapping(value = "/example2", method = RequestMethod.GET)
	public SimpleExampleClass example2(@RequestParam String name) {
		return new SimpleExampleClass("Hallo " + name, 23);
	}

	/**
	 * BEISPIEL-POST-METHODE: Um der Anwendung Informationen übergeben zu können,
	 * braucht man ein POST (oder ein PUT). Wenn Sie also beispielsweise im Kalender
	 * einen neuen Eintrag vornehmen wollen.
	 * 
	 * Im Browser direkt kann man nur GET-Requests absetzen. Daher brauchen wir
	 * hierfür nun Swagger: http://localhost:8080/swagger-ui/
	 *
	 * Beispielanwendungsfall: Sie wollen eine neue Bühne hinzufügen.
	 * 
	 * @return The content of a class
	 */
	@RequestMapping(value = "/example3", method = RequestMethod.POST)
	public void example3(@RequestParam String exampleParam) {
		System.out.println("Dieser Text wurde empfangen:" + exampleParam);
	}

	/**
	 * BEISPIEL-POST-METHODE: Man kann sogar ganze Klassen als Parameter übergeben
	 * lassen.
	 * 
	 * Schauen Sie es sich einfach mal in Swagger an:
	 * http://localhost:8080/swagger-ui/
	 * 
	 * Beispielanwendungsfall: Sie wollen einen neuen Termin eintragen lassen.
	 * 
	 * @return The content of a class
	 */
	@RequestMapping(value = "/example4", method = RequestMethod.POST)
	public void example4(@RequestBody SimpleExampleClass simpleClassParameter) {
		System.out.println(simpleClassParameter);
	}

	@RequestMapping(value = "/test", method = RequestMethod.GET)
	public String home(@RequestParam String name) {
		String salutation = "Hallo " + name + "!";
		return salutation;
	}

	@RequestMapping(value = "/ping", method = RequestMethod.GET)
	public String home() {
		return "Heute ist ein wirklich toller Tag!";
	}

}
