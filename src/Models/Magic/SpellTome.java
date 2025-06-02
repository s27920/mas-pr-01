package Models.Magic;

import Models.Util.SuperObject;
import Models.Wizard;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

public class SpellTome extends SuperObject {
    private Wizard ownedBy;
    private final Set<SpellTomePage> pages;

    private String title;
    private String author;
    private LocalDate publicationDate;

    public SpellTome(String title, String author, LocalDate publicationDate) {
        this.title = title;
        this.author = author;
        this.publicationDate = publicationDate;
        this.pages = new HashSet<>();
    }

    public void addPage(SpellTomePage page){
        if(pages.add(page)){
            page.setSpellTome(this);
        }
    }

    public Wizard getOwnedBy() {
        return ownedBy;
    }

    @Override
    public void removeObj() {
        for (SpellTomePage page:pages) {
            page.removeObj();
        }
        super.removeObj();
    }

    public int getPageCount(){
        return this.pages.size();
    }

    public void setOwnedBy(Wizard ownedBy) {
        this.ownedBy = ownedBy;
    }
}
