package org.blackpanther.ecosystem.gui.wizard;

import com.nexes.wizard.Wizard;
import com.nexes.wizard.WizardController;

/**
 * @author MACHIZAUD Andréa
 * @version 0.2 - Wed May 11 02:54:46 CEST 2011
 */
public class CreationController
        extends WizardController {
    /**
     * This constructor accepts a reference to the Wizard component that created it,
     * which it uses to update the button components and access the WizardModel.
     *
     * @param w A callback to the Wizard component that created this controller.
     */
    public CreationController(Wizard w) {
        super(w);
    }
}
