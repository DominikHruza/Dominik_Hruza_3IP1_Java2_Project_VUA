package hr.dhruza.viewmodel;

import hr.dhruza.utils.constants.DieConstants;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

import java.util.Random;

public class DieViewModel {

  private final IntegerProperty value;

  public DieViewModel() {
    this.value = new SimpleIntegerProperty();
  }

  public void roll() {
    Random r = new Random();
    int result = r.nextInt(DieConstants.MAX_VALUE);
    result++;
    setValue(result);
  }

  public Integer getValue() {
    return value.get();
  }

  public void setValue(Integer value) {
    this.value.set(value);
  }

  public IntegerProperty valueProperty() {
    return value;
  }
}
