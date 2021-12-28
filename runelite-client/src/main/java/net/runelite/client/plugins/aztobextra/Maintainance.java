//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package net.runelite.client.plugins.aztobextra;

import java.util.ArrayList;
import java.util.Arrays;
import net.runelite.api.Projectile;

public class Maintainance {
    public static ArrayList<Integer> magicWeaponId = new ArrayList(Arrays.asList(12899, 20736, 2417, 2416, 2415, 22323, 6562, 11998, 4675, 22292, 21006, 6914, 12422, 6912, 6910, 6908, 1393, 3053, 11787, 20730, 1401, 3054, 24422, 24423, 24424, 24425, 11905, 11907, 22288, 25731, 4710, 4862, 4863, 4864, 4865));
    public static ArrayList<Integer> rangeWeaponId = new ArrayList(Arrays.asList(12926, 20997, 12788, 11235, 19478, 19481, 21012, 21902, 11785, 10156, 9185, 8880, 4934, 4935, 4936, 4937, 11959, 9977, 861, 4212, 4214, 4215, 4216, 4217, 4218, 4219, 4220, 4221, 4222, 4223, 11748, 11749, 11750, 11751, 11752, 11753, 11754, 11755, 11756, 11757, 11758, 806, 807, 808, 809, 810, 811, 11230, 11959, 10034, 25862, 25865, 25867, 25869, 25884, 25886, 25888, 25890, 25892, 25894, 25896, 4734, 4934, 3935, 4936, 4937));
    public static ArrayList<Integer> meleeWeaponId = new ArrayList(Arrays.asList(23360, 12006, 22324, 22325, 13576, 22978, 21003, 23987, 20370, 4587, 21009, 12809, 19675, 21219, 21015, 4910, 4982, 20727, 4153, 10887, 5698, 3204, 11824, 11802, 18804, 11806, 11808, 20366, 20368, 20372, 20374, 21646, 21742, 1333, 20000, 11889, 22731, 22734, 22486, 11838, 13263, 4151, 12773, 12774, 22840, 11037, 23995, 23997, 24219, 24551, 24553, 13652, 1215, 1231, 24417, 22542, 22545, 11804, -1, 25739, 25741, 25736, 25738, 25734, 25870, 25872, 25874, 25876, 25878, 25880, 25882, 4718, 4886, 4887, 4888, 4889, 4755, 4982, 4983, 4984, 4985, 4726, 4910, 4911, 4912, 4913, 4747, 4958, 4959, 4960, 4961, 12727, 22613, 22615, 24617, 24619));
    public static boolean ignoreChins = true;

    public Maintainance() {
    }

    public static int getInteractingIndex(Projectile p) {
        return ClientInterface.getInteractingIndex(p);
    }
}
