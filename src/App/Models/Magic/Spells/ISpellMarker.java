package App.Models.Magic.Spells;

public interface ISpellMarker<T> {
    T getValues();

    void removeObj();
}
