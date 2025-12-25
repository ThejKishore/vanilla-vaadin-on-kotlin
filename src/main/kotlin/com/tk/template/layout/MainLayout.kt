package com.example.karibudsl.com.tk.template.layout

import com.example.karibudsl.com.tk.template.themes.RadioButtonTheme
import com.vaadin.flow.component.AbstractField.ComponentValueChangeEvent
import com.vaadin.flow.component.HasValue.ValueChangeListener
import com.vaadin.flow.component.applayout.AppLayout
import com.vaadin.flow.component.applayout.DrawerToggle
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.html.Footer
import com.vaadin.flow.component.html.H1
import com.vaadin.flow.component.html.Header
import com.vaadin.flow.component.html.Span
import com.vaadin.flow.component.icon.SvgIcon
import com.vaadin.flow.component.orderedlayout.Scroller
import com.vaadin.flow.component.popover.Popover
import com.vaadin.flow.component.radiobutton.RadioButtonGroup
import com.vaadin.flow.component.radiobutton.RadioGroupVariant
import com.vaadin.flow.component.sidenav.SideNav
import com.vaadin.flow.component.sidenav.SideNavItem
import com.vaadin.flow.router.AfterNavigationEvent
import com.vaadin.flow.router.AfterNavigationObserver
import com.vaadin.flow.router.Layout
import com.vaadin.flow.server.menu.MenuConfiguration
import com.vaadin.flow.theme.lumo.Lumo
import com.vaadin.flow.theme.lumo.LumoUtility
import java.util.*


@Layout
class MainLayout : AppLayout(), AfterNavigationObserver {
    private var viewTitle: H1? = null
    private var theme = ""
    private var colorScheme = ""
    private var density = ""


    init {
        setPrimarySection(Section.DRAWER)
        addDrawerContent()
        addHeaderContent()
    }

    fun addDrawerContent() {
        val appName = Span("My App")
        appName.addClassNames(LumoUtility.FontSize.LARGE, LumoUtility.FontWeight.BOLD)

        val header = Header(appName)

        val scroller = Scroller(createNavigation())
        addToDrawer(header, scroller, createFooter())
    }

    private fun createNavigation() = SideNav().apply {
        try {
            MenuConfiguration.getMenuEntries().forEach { e ->
                if (e.icon != null) {
                    this.addItem(SideNavItem(e.title, e.path, SvgIcon(e.icon)))
                } else {
                    this.addItem(SideNavItem(e.title, e.path))
                }
            }
        } catch (e: Exception) {
            // In tests or if no menu is configured, this might fail.
        }
    }

    private fun createFooter() =  Footer()

    fun addHeaderContent() {
        val toggle = DrawerToggle()
        toggle.setAriaLabel("Menu toggle")

        viewTitle = H1()
        viewTitle?.addClassNames(LumoUtility.FontSize.LARGE, LumoUtility.Margin.NONE)


        val themeIcon = Button("Theme")
        themeIcon.addClassNames(LumoUtility.Flex.AUTO , LumoUtility.Padding.SMALL)
        themeIcon.addClickListener {

            // Theme
            val colorScheme = RadioButtonGroup<String?>()
            colorScheme.addClassNames(LumoUtility.BoxSizing.BORDER, LumoUtility.Padding.XSMALL)
            colorScheme.addThemeNames(RadioButtonTheme.EQUAL_WIDTH, RadioButtonTheme.PRIMARY, RadioButtonTheme.TOGGLE)
            colorScheme.addValueChangeListener(ValueChangeListener { e: ComponentValueChangeEvent<RadioButtonGroup<String?>?, String?>? ->
                setColorScheme(
                    e!!.getValue() == Lumo.DARK
                )
            })

            colorScheme.setAriaLabel("Color scheme")
            colorScheme.setTooltipText("Color scheme")

            colorScheme.setItems(Lumo.LIGHT, Lumo.DARK)
            colorScheme.setValue(Lumo.LIGHT)
            colorScheme.setWidthFull()
            // Density
            val density = RadioButtonGroup<String?>()
            density.addClassNames(LumoUtility.BoxSizing.BORDER, LumoUtility.Padding.XSMALL)
            density.addThemeNames(RadioButtonTheme.EQUAL_WIDTH, RadioButtonTheme.PRIMARY, RadioButtonTheme.TOGGLE)
            density.addValueChangeListener(ValueChangeListener { e: ComponentValueChangeEvent<RadioButtonGroup<String?>?, String?>? ->
                setDensity(
                    e!!.getValue() == "Compact"
                )
            })

            density.setAriaLabel("Density")
            density.setTooltipText("Density")

            density.setItems("Default", "Compact")
            density.setValue("Default")
            density.setWidthFull()

            val theme = RadioButtonGroup<String?>()
            theme.addClassNames(LumoUtility.BoxSizing.BORDER, LumoUtility.Padding.XSMALL)
            theme.addThemeVariants(RadioGroupVariant.LUMO_VERTICAL)
            theme.addValueChangeListener(ValueChangeListener { e: ComponentValueChangeEvent<RadioButtonGroup<String?>?, String?>? ->
                setTheme(e!!.getValue()!!)
            })

            theme.setAriaLabel("Theme")
            theme.setTooltipText("Theme")

            theme.setItems("Lumo", "Material", "Carbon", "Radix")
            theme.setValue("Lumo")
            theme.setWidthFull()
            val popover = Popover(colorScheme , density ,theme)
            popover.target = themeIcon
            popover.open()

        }
        // Theme


        addToNavbar(true, toggle, viewTitle , themeIcon)
    }

    override fun afterNavigation(event: AfterNavigationEvent?) {
        viewTitle?.text = getCurrentPageTitle()
    }

    fun getCurrentPageTitle(): String? {
        return MenuConfiguration.getPageHeader(content).orElse("")
    }

    private fun setColorScheme(dark: Boolean) {
        this.colorScheme = if (dark) Lumo.DARK else Lumo.LIGHT
        updateTheme()
    }

    private fun setDensity(compact: Boolean) {
        this.density = if (compact) "compact" else ""
        updateTheme()
    }

    private fun setTheme(theme: String) {
        this.theme = theme.lowercase(Locale.getDefault())
        updateTheme()
    }

    private fun updateTheme() {
        val js = "document.documentElement.setAttribute('theme', $0)"
        getElement().executeJs(js, this.colorScheme + " " + this.density + " " + this.theme)
    }

}