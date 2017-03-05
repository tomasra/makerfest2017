int led2 = 12;
int laikas = 1;

void setup() {
    pinMode(led1, OUTPUT);
    pinMode(led2, OUTPUT);
    }

void ledas1() {
    digitalWrite(led1, 1);
    delay(laikas);
    digitalWrite(led1, 0);
    }
                     
void ledas2() {
    digitalWrite(led2, 1);
    delay(laikas);
    digitalWrite(led2, 0);
    }
                                 
void loop()  {
    ledas1();
    ledas2();
    delay(laikas * 7);
    }                                        }
