package com.example.karibudsl.com.tk.template.view

import com.github.mvysny.karibudsl.v10.KComposite
import com.github.mvysny.karibudsl.v10.button
import com.github.mvysny.karibudsl.v10.onClick
import com.github.mvysny.karibudsl.v10.textField
import com.github.mvysny.karibudsl.v10.verticalLayout
import com.github.mvysny.kaributools.setPrimary
import com.vaadin.flow.component.Key
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.notification.Notification
import com.vaadin.flow.component.textfield.TextField
import com.vaadin.flow.router.Menu
import com.vaadin.flow.router.PageTitle
import com.vaadin.flow.router.Route
import eu.vaadinonkotlin.restclient.VokRestClient
import eu.vaadinonkotlin.restclient.buildRequest
import eu.vaadinonkotlin.restclient.buildUrl
import eu.vaadinonkotlin.restclient.exec

/**
 * The main view contains a button and a click listener.
 */
@PageTitle("Main")
@Route("/greetings")
@Menu(order = 1.0, title = "Greetings")
class MainView : KComposite() {
    private lateinit var nameField: TextField
    private lateinit var greetButton: Button

    // The main view UI definition
    private val root = ui {
        // Use custom CSS classes to apply styling. This is defined in styles.css.
        verticalLayout(classNames = "centered-content") {

            // Use TextField for standard text input
            nameField = textField("Your name") {
                addClassName("bordered")
            }

            // Use Button for a clickable button
            greetButton = button("Say hello") {
                setPrimary(); addClickShortcut(Key.ENTER)
            }
        }
    }

    init {
        // attach functionality to the UI components.
        // It's a good practice to keep UI functionality separated from UI definition.

        // Button click listeners can be defined as lambda expressions
        greetButton.onClick {
            val vokRestClient = VokRestClient.httpClient
            val request = "http://localhost:7070/plt/configserver/health".buildUrl().buildRequest()
            val response = vokRestClient.exec(request){ t -> t.body().buffered().reader().readText()}
            val fromJson = VokRestClient.gson.fromJson(response, Map::class.java)
            Notification.show("$response && $fromJson" )
        }
    }
}