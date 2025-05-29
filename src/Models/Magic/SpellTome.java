package Models.Magic;

import Models.Util.SuperObject;
import Models.Wizard;

import java.util.Set;

public class SpellTome extends SuperObject {
    private Wizard wizard;
    private Set<SpellTomePage> pages;

    public SpellTome(Wizard wizard, Set<SpellTomePage> pages) {
        this.wizard = wizard;
        this.pages = pages;
    }

    public void addPage(SpellTomePage page){
        if(pages.add(page)){
            page.setSpellTome(this);
        }
    }

    @Override
    public void removeObj() {
        for (SpellTomePage page:pages) {
            page.removeObj();
        }
        super.removeObj();
    }

    public void setWizard(Wizard wizard) {
        this.wizard = wizard;
    }
}
