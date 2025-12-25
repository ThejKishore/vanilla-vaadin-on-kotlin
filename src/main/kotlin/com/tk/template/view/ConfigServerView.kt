package com.example.karibudsl.com.tk.template.view

import com.example.karibudsl.com.tk.template.dto.ConfigDetials
import com.github.mvysny.karibudsl.v10.KComposite
import com.github.mvysny.karibudsl.v10.isExpand
import com.github.mvysny.karibudsl.v10.textField
import com.github.mvysny.karibudsl.v10.verticalLayout
import com.github.mvysny.kaributools.addColumnFor
import com.vaadin.flow.component.Component
import com.vaadin.flow.component.contextmenu.ContextMenu
import com.vaadin.flow.component.contextmenu.MenuItem
import com.vaadin.flow.component.contextmenu.SubMenu
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.icon.Icon
import com.vaadin.flow.component.icon.VaadinIcon
import com.vaadin.flow.component.menubar.MenuBar
import com.vaadin.flow.component.notification.Notification
import com.vaadin.flow.component.textfield.TextField
import com.vaadin.flow.router.Menu
import com.vaadin.flow.router.PageTitle
import com.vaadin.flow.router.Route


@PageTitle("Config Server")
@Route("configserver")
@Menu(order = 2.0, title = "Config Server")
class ConfigServerView : KComposite() {

    private lateinit var grid : Grid<ConfigDetials>
    private lateinit var inputFeiled : TextField

    private val root = ui {
        verticalLayout(classNames = "centered-content") {
            inputFeiled = textField("Searched") {
                isExpand = false

            }
            grid = Grid<ConfigDetials>(ConfigDetials::class.java, false)
            grid.apply {
                isExpand = true
                addColumnFor(ConfigDetials::appName).setHeader("App Name")
                addColumnFor(ConfigDetials::propertyKey).setHeader("Property Key")
                addColumnFor(ConfigDetials::propertyValue).setHeader("Property Value")
                addComponentColumn { data -> createButton(data) }.setHeader("Action")
            }

            add(grid)
            grid
        }
    }

    fun setConfigDetials(configDetials: List<ConfigDetials>) {
        grid.setItems(configDetials)
    }

    init {
        val configDetails = listOf(
            ConfigDetials("app1", "key1", "value1"),
            ConfigDetials("app1", "key2", "value2"),
            ConfigDetials("app1", "key3", "value3")
        )
        setConfigDetials(configDetails)

        inputFeiled.addValueChangeListener {
            val configDetials = configDetails
            grid.setItems(configDetials.filter { it.propertyKey.contains(inputFeiled.value) })
        }
    }


    fun createButton(configDetials: ConfigDetials): Component {
        val button = MenuBar() // Three dots menu button
        var menuItem = button.addItem(":")
        val subMenu: SubMenu = menuItem.subMenu
         subMenu.addItem("View") { clickEvent ->  Notification.show("View ${configDetials.appName}", 3000, Notification.Position.TOP_START)}
         subMenu.addItem("Delete") {}
         subMenu.addItem("Download") {}
         subMenu.addItem("Share") {}
         subMenu.addItem("Archive") {}
        return button

    }



    private fun createIconItem(
        menu: MenuBar, iconName: VaadinIcon?,
        ariaLabel: String?
    ): MenuItem {
        val icon: Icon = Icon(iconName)
        val item: MenuItem = menu.addItem(icon)
        item.setAriaLabel(ariaLabel)
        return item
    }

}