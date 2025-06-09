package App.Util;

import App.Models.Magic.KnownSpell;

import java.io.Serializable;
import java.util.Comparator;

public class KnownSpellComparator implements Comparator<KnownSpell>, Serializable {
    @Override
    public int compare(KnownSpell ks1, KnownSpell ks2) {
        int masteryCompare = Integer.compare(ks2.getMasteryLevel(), ks1.getMasteryLevel());
        if (masteryCompare != 0) return masteryCompare;

        int nameCompare = ks1.getSpell().getName().compareTo(ks2.getSpell().getName());
        if (nameCompare != 0) return nameCompare;

        return Integer.compare(ks1.hashCode(), ks2.hashCode());
    }
}

