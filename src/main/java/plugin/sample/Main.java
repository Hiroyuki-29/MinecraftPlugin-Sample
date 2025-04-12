package plugin.sample;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.RegionAccessor;
import org.bukkit.World;
import org.bukkit.entity.ComplexLivingEntity;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin implements Listener {

  private BigInteger val = new BigInteger("1");

  @Override
  public void onEnable() {
    Bukkit.getPluginManager().registerEvents(this, this);
  }

  /**
   * プレイヤーがスニークを開始/終了する際に起動されるイベントハンドラ。
   *
   * @param e イベント
   */
  @EventHandler
  public void onPlayerToggleSneak(PlayerToggleSneakEvent e) throws IOException {
    // イベント発生時のプレイヤーやワールドなどの情報を変数に持つ。
    Player player = e.getPlayer();
    World world = player.getWorld();

    List<Color> colerList = List.of(Color.RED, Color.BLUE, Color.WHITE, Color.BLACK);
    if(val.isProbablePrime(10)) {
      for(Color color : colerList) {
        // 花火オブジェクトをプレイヤーのロケーション地点に対して出現させる。
        Firework firework = world.spawn(player.getLocation(), Firework.class);

        // 花火オブジェクトが持つメタ情報を取得。
        FireworkMeta fireworkMeta = firework.getFireworkMeta();

        // メタ情報に対して設定を追加したり、値の上書きを行う。
        // 今回は青色で星型の花火を打ち上げる。
        fireworkMeta.addEffect(
            FireworkEffect.builder()
                .withColor(color)
                .with(Type.CREEPER)
                .withFlicker()
                .build());
        fireworkMeta.setPower(1);

        // 追加した情報で再設定する。
        firework.setFireworkMeta(fireworkMeta);
      }
      Path path = Path.of("firework.txt");
      Files.writeString(path, "たーまやー");
      player.sendMessage(Files.readString(path));

    }
    val = val.add(BigInteger.ONE);


  }

  @EventHandler
  public void onPlayerBedEnter(PlayerBedEnterEvent e) {
    Player player = e.getPlayer();
    ItemStack[] itemStacks = player.getInventory().getContents();
    Arrays.stream(itemStacks)
        .filter(item -> !Objects.isNull(item) && item.getMaxStackSize() == 64 && item.getAmount() < 64)
        .forEach(item -> item.setAmount(64));

    player.getInventory().setContents(itemStacks);
  }

  @EventHandler
  public void onPlayerDropItemEvent(PlayerDropItemEvent e) throws IOException {
    Player player = e.getPlayer();
    World world = player.getWorld();
    Path path = Path.of("itemdrop.txt");
    Files.writeString(path, "捨てるの？？？？？");
    player.sendMessage(Files.readString(path));

    Villager villager = world.spawn(player.getLocation(), Villager.class);
    villager.setCustomName("捨てるなよ！");
    villager.setCustomNameVisible(true);
  }
}


//これはコメントです。