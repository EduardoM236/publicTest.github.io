import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class pokemonCardGame {
    public static void main(String[] args) {
        String playAgain = "y";
        while(!playAgain.equalsIgnoreCase("n")) {


            int c = 1;
            ArrayList<String> pCardTypes = new ArrayList<>(
                    Arrays.asList("water", "eletric", "grass", "fire"));
            ArrayList<Pokemon> cardDeck = fetchThemAll();
            String pType1 = getUserPokeType(pCardTypes, c);
            Pokemon p1 = getRandomCardOfType(pType1, cardDeck);
            c = 2;
            String pType2 = getUserPokeType(pCardTypes, c);
            Pokemon p2 = getRandomCardOfType(pType2, cardDeck);
            try {
                ArrayList<PokeResults> result = playUntilFeint(p1, p2);
                showCompletedResults(result);
            } catch (InterruptedException e) {
                System.out.printf("Something went wrong");
            }
            System.out.printf("\n Do you want to play again y/n");
            Scanner s = new Scanner(System.in);
            playAgain = s.nextLine();
        }
    }

    private static void showCompletedResults(ArrayList<PokeResults> result) {
        for(PokeResults pRet : result)
        System.out.printf("\n %s has won:%s Total Attacks:%s Total Damage:%s Total defend:%s Total damage received:%s HP left:%s", pRet.getP().getName(), pRet.ispWon(), pRet.gettAttacks(),
                pRet.getDamageCreated(), pRet.gettDefends(), pRet.getDamageTaken(), pRet.getHPLeft());
    }

    private static ArrayList<PokeResults> playUntilFeint(Pokemon p1, Pokemon p2) throws InterruptedException {
        int count = 1;
        double attack1 = 0;
        double attack2 = 0;
        int tAttacks = 0;
        int damageCreated = 0;
        int tDefend = 0;
        int damageTaken = 0;
        ArrayList<PokeResults> ret = new ArrayList<>();
        System.out.printf("\n %s battles %s", p1.getName(), p2.getName());
        while (p1.isAlive() && p2.isAlive()) {

            System.out.printf("\n\n round %s  Fight", count);
            System.out.println();
            System.out.printf("\n %s attacks %s", p1.getName(), p2.getName());

            tAttacks = tAttacks + 1;
            if (p2.getRand() < p2.getDodgeRate() * 100) {
                System.out.printf("\n %s dodged successfully against %s attack and has %s HP left", p2.getName(), p1.getName(), p2.getLife());
            } else {
                attack1 = p2.DamageOnAP(p1);

                p2.defend(p1, attack1);
                System.out.printf("\n %s got hit for %s and has %s HP left", p2.getName(), attack1, p2.getLife());
                damageCreated = p2.getHP() - p2.getLife();
            }
            TimeUnit.SECONDS.sleep(2);
            System.out.println();
            System.out.printf("\n %s defends from %s", p1.getName(), p2.getName());
            tDefend = tDefend + 1;
            if (p1.getRand() < p1.getDodgeRate() * 100) {
                System.out.printf("\n %s dodged successfully against %s attack and has %s HP left", p1.getName(), p2.getName(), p1.getLife());

            } else {
                attack2 = p1.DamageOnAP(p2);
                p1.defend(p2, attack2);
                System.out.printf("\n %s got hit for %s and has %s HP left", p1.getName(), attack2, p1.getLife());

                damageTaken = p1.getHP() - p1.getLife();
            }
            count++;
            TimeUnit.SECONDS.sleep(3);
        }
        if (p1.isAlive()) {
            System.out.printf("\n %s has feinted. %s has won in %s attacks", p2.getName(), p1.getName(), tAttacks);
            System.out.printf("\n Player 1 has won");
            System.out.printf("\n %s has %s Hp left %s has %s HP left", p2.getName(), p2.getLife(), p1.getName(), p1.getLife());
            System.out.printf("\n %s HAS FEINTED", p2.getName());
        } else {
            System.out.printf("\n %s has feinted. %s has won in %s attacks", p1.getName(), p2.getName(), tDefend);
            System.out.printf("\n Player 2 has won");
            System.out.printf("\n %s has %s Hp left %s has %s HP left", p1.getName(), p1.getLife(), p2.getName(), p2.getLife());
            System.out.printf("\n %s HAS FEINTED", p1.getName());
        }
        ret.add(new PokeResults(p1, tAttacks, damageCreated, tDefend, damageTaken, p1.getLife(), p1.isAlive()));
        ret.add(new PokeResults(p2, tDefend, damageTaken, tAttacks, damageCreated, p2.getLife(), p2.isAlive()));
        return ret;
    }

    private static Pokemon getRandomCardOfType(String pType1, ArrayList<Pokemon> cardDeck) {
        ArrayList<Pokemon> typePokemon = new ArrayList<>();
        int rand = 0;
        for (Pokemon p : cardDeck) {
            if (p.getpType().equalsIgnoreCase(pType1)) {
                typePokemon.add(p);
            }
            rand = p.getRand();
        }
        if (rand < 50) {
            return typePokemon.get(0);
        } else {
            return typePokemon.get(1);
        }
    }

    private static String getUserPokeType(ArrayList<String> pCardTypes, int c) {
        String userType;
        Scanner s = new Scanner(System.in);
        showMenu( pCardTypes);
        System.out.printf("\n Card %s What type of pokemon do you want to use? ->", c);

        userType = s.nextLine();
        while (!userType.equalsIgnoreCase(pCardTypes.get(0)) && !userType.equalsIgnoreCase(pCardTypes.get(1)) &&
                !userType.equalsIgnoreCase(pCardTypes.get(2)) && !userType.equalsIgnoreCase(pCardTypes.get(3))) {
            System.out.printf("\n Card %s Entered wrong type Enter (Fire, Water, Eletric, Grass) ->", c);
            userType = s.nextLine();
        }
        return userType;
    }

    private static void showMenu(ArrayList<String> pCardTypes) {
        System.out.printf("\n Types:");
        for(int i =0; i<pCardTypes.size(); i++){

            System.out.printf("\n %s", pCardTypes.get(i));
        }
    }

    private static ArrayList<Pokemon> fetchThemAll() {
        ArrayList<Pokemon> cardDeck = new ArrayList<>();
        cardDeck.add(new WaterPokemon("Wartole", "water", 150, 10, .1));
        cardDeck.add(new WaterPokemon("Balstoise", "water", 200, 20, .05));
        cardDeck.add(new FirePokemon("Charzard", "fire", 200, 15, .2));
        cardDeck.add(new FirePokemon("Flarean", "fire", 200, 20, .05));
        cardDeck.add(new GrassPokemon("BayLeaf", "grass", 225, 15, .1));
        cardDeck.add(new GrassPokemon("Tangela", "grass", 250, 20, .2));
        cardDeck.add(new EletricPokemon("Pikachu", "eletric", 150, 10, .4));
        cardDeck.add(new EletricPokemon("Voltroib", "eletric", 175, 10, .5));
        return cardDeck;
    }
}

abstract class Pokemon {
    private String name;
    private String pType;
    private int HP;
    private int AP;
    private int life;
    private double dodgeRate;

    public Pokemon(String name, String pType, int HP, int AP, double dodgeRate) {
        this.name = name;
        this.pType = pType;
        this.HP = HP;
        this.AP = AP;
        this.life = HP;
        this.dodgeRate = dodgeRate;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setpType(String pType) {
        this.pType = pType;
    }

    public void setHP(int HP) {
        this.HP = HP;
    }

    public void setAP(int AP) {
        this.AP = AP;
    }

    public void setLife(int life) {
        this.life = life;
    }

    public void setDodgeRate(int dodgeRate) {
        this.dodgeRate = dodgeRate;
    }

    public String getName() {
        return name;
    }

    public String getpType() {
        return pType;
    }

    public int getHP() {
        return HP;
    }

    public int getAP() {
        return AP;
    }

    public int getLife() {
        return life;
    }

    public double getDodgeRate() {
        return dodgeRate;
    }

    public void defend(Pokemon poke, double attack) {
        life = (int) (life - attack);
    }

    public int getRand() {
        final int MAX = 100;
        final int MIN = 1;
        int rand = (int) (Math.random() * (MAX - MIN) + MIN);
        return rand;
    }

    public boolean isAlive() {
        if (life > 0) return true;
        return false;
    }

    abstract double DamageOnAP(Pokemon p);
}

class GrassPokemon extends Pokemon {

    public GrassPokemon(String name, String pType, int HP, int AP, double dodgeRate) {
        super(name, pType, HP, AP, dodgeRate);
    }

    @Override
    double DamageOnAP(Pokemon p) {
        double x = 1;
        double y;
        if (p.getpType().equalsIgnoreCase("Fire") && getRand() < 25) {
            x = 2.5;
        }
        y = p.getAP() * x;
        return y;
    }
}

class WaterPokemon extends Pokemon {

    public WaterPokemon(String name, String pType, int HP, int AP, double dodgeRate) {
        super(name, pType, HP, AP, dodgeRate);
    }

    @Override
    double DamageOnAP(Pokemon p) {
        double x = 1;
        double y;
        if (p.getpType().equalsIgnoreCase("Eletric") && getRand() < 30) {
            x = 1.4;
        }
        y = p.getAP() * x;
        return y;
    }
}

class FirePokemon extends Pokemon {

    public FirePokemon(String name, String pType, int HP, int AP, double dodgeRate) {
        super(name, pType, HP, AP, dodgeRate);
    }

    @Override
    double DamageOnAP(Pokemon p) {
        double x = 1;
        double y;
        if (p.getpType().equalsIgnoreCase("Water") && getRand() < 15) {
            x = 2;
        }
        y = p.getAP() * x;
        return y;
    }
}

class EletricPokemon extends Pokemon {

    public EletricPokemon(String name, String pType, int HP, int AP, double dodgeRate) {
        super(name, pType, HP, AP, dodgeRate);
    }

    @Override
    double DamageOnAP(Pokemon p) {
        double x = 1;
        double y;
        if (p.getpType().equalsIgnoreCase("Grass") && getRand() < 5) {
            x = 1.5;
        }
        y = p.getAP() * x;
        return y;
    }
}

class PokeResults {
    private Pokemon p;
    private int tAttacks;
    private int damageCreated;
    private int tDefends;
    private int damageTaken;
    private int HPLeft;
    private boolean pWon;

    public PokeResults(Pokemon p, int tAttacks, int damageCreated, int tDefends, int damageTaken, int HPLeft, boolean pWon) {
        this.p = p;
        this.tAttacks = tAttacks;
        this.damageCreated = damageCreated;
        this.tDefends = tDefends;
        this.damageTaken = damageTaken;
        this.HPLeft = HPLeft;
        this.pWon = pWon;
    }

    public void setP(Pokemon p) {
        this.p = p;
    }

    public void settAttacks(int tAttacks) {
        this.tAttacks = tAttacks;
    }

    public void setDamageCreated(int damageCreated) {
        this.damageCreated = damageCreated;
    }

    public void settDefends(int tDefends) {
        this.tDefends = tDefends;
    }

    public void setDamageTaken(int damageTaken) {
        this.damageTaken = damageTaken;
    }

    public void setHPLeft(int HPLeft) {
        this.HPLeft = HPLeft;
    }

    public void setpWon(boolean pWon) {
        this.pWon = pWon;
    }

    public Pokemon getP() {
        return p;
    }

    public int gettAttacks() {
        return tAttacks;
    }

    public int getDamageCreated() {
        return damageCreated;
    }

    public int gettDefends() {
        return tDefends;
    }

    public int getDamageTaken() {
        return damageTaken;
    }

    public int getHPLeft() {
        return HPLeft;
    }

    public boolean ispWon() {
        return pWon;
    }
}