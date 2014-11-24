package com.vaadin.grails.server

/**
 * @author Stephan Grundner
 */
class SecuredMapping extends DefaultMapping {

    String[] access

    Map<String, String[]> accessByFragment = new HashMap()

    String[] getFragmentAccess(String fragment) {
        accessByFragment.get(fragment)
    }

    void addFragmentAccess(String fragment, String[] access) {
        accessByFragment.put(fragment, access)
    }
}
