package com.example.karibudsl.com.tk.template.layout

import com.vaadin.flow.component.applayout.AppLayout
import com.vaadin.flow.component.applayout.DrawerToggle
import com.vaadin.flow.component.html.Footer
import com.vaadin.flow.component.html.H1
import com.vaadin.flow.component.html.Header
import com.vaadin.flow.component.html.Span
import com.vaadin.flow.component.icon.SvgIcon
import com.vaadin.flow.component.orderedlayout.Scroller
import com.vaadin.flow.component.sidenav.SideNav
import com.vaadin.flow.component.sidenav.SideNavItem
import com.vaadin.flow.router.AfterNavigationEvent
import com.vaadin.flow.router.AfterNavigationObserver
import com.vaadin.flow.router.Layout
import com.vaadin.flow.server.menu.MenuConfiguration
import com.vaadin.flow.theme.lumo.LumoUtility


@Layout
class MainLayout : AppLayout(), AfterNavigationObserver {
    private var viewTitle: H1? = null

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

        addToNavbar(true, toggle, viewTitle)
    }

    override fun afterNavigation(event: AfterNavigationEvent?) {
        viewTitle?.text = getCurrentPageTitle()
    }

    fun getCurrentPageTitle(): String? {
        return MenuConfiguration.getPageHeader(content).orElse("")
    }
}