<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta name="package-mapping"
          content="g:org.vaadin.grails.ui"/>
</head>
<body>
<v-panel style-name="borderless">
    <v-vertical-layout margin spacing>

        <g-breadcrumb-trail _id="breadcrumbTrail"></g-breadcrumb-trail>

        <v-label style-name="h1 colored">
            Vaadin 7 Plugin Demos
        </v-label>

        <v-horizontal-layout spacing size-full>

            <v-panel caption="Demo 1" :center size-full>
                <v-vertical-layout margin spacing>
                    <v-label>
                        Shows how to use UIs and Views
                    </v-label>
                    <v-label style-name="light">
                        See com.vaadin.grails.Vaadin#navigateTo()
                    </v-label>
                    <v-button _id="button1">Goto</v-button>
                </v-vertical-layout>
            </v-panel>

            <v-panel caption="Demo 2" :center size-full>
                <v-vertical-layout margin spacing>
                    <v-label>
                        Shows how UI scoped attributes are working
                    </v-label>

                    <v-button _id="button2">Goto</v-button>
                </v-vertical-layout>
            </v-panel>

            <v-panel caption="Demo 3" :center>
                <v-vertical-layout margin spacing>
                    <v-label>
                        Books
                    </v-label>

                    <v-button _id="button3">Goto</v-button>
                </v-vertical-layout>
            </v-panel>

        </v-horizontal-layout>


    </v-vertical-layout>
</v-panel>
</body>
</html>
