package net.sivils.stringutils;

import org.bukkit.OfflinePlayer;
import me.clip.placeholderapi.PlaceholderAPI;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Random;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class StringUtils extends PlaceholderExpansion {


    @Override
    public @NotNull String getIdentifier() {
        return "stringutils";
    }

    @Override
    public @NotNull String getAuthor() {
        return "DuneSciFye";
    }

    @Override
    public @NotNull String getVersion() {
        return "1.0.0";
    }

    @Override
    public @Nullable String onRequest(OfflinePlayer player, @NotNull String args) {
        final String[] parts = args.split("_", 2);

        if (parts.length <= 1) {
            return null;
        }

        String arguments = PlaceholderAPI.setBracketPlaceholders(player, parts[1]);

        switch (parts[0]){
            case "inputoutput":
                String[] split = arguments.split(",");
                int length = split.length;
                if (length%2==1&&length>1){
                    for (int i = 0; i<(length-1)/2; i++)
                        if (Objects.equals(split[0], split[1 + i * 2])) {
                            return split[2+i*2];
                        }
                    return "";
                }
                return "Incorrect argument amount.";
            case "random":
                arguments = "_" + arguments;
                if (!arguments.contains("_type:")||((arguments.contains("_type:int")||arguments.contains("_type:double"))&&(!arguments.contains("_min:")||!arguments.contains("_max:")))||(arguments.contains("_type:string")&&!arguments.contains("_text:"))){
                    return "Invalid arguments.";
                }
                String regex = "(_seed:|_text:|_round:|_separator:|_min:|_max:|_type:)(.+?)(?=(_seed:|_text:|_round:|_separator:|_min:|_max:|_type:|$))";

                Pattern pattern = Pattern.compile(regex);
                Matcher matcher = pattern.matcher(arguments);

                String type = null, text = null, separator = ",";
                double min = 0, max = 0;
                long seed = 0;
                int round = 2;

                while (matcher.find()) {
                    String key = matcher.group(1);
                    String value = matcher.group(2);

                    switch (key) {
                        case "_seed:":
                            seed = Long.parseLong(value);
                            break;
                        case "_min:":
                            min = Double.parseDouble(value);
                            break;
                        case "_max:":
                            max = Double.parseDouble(value);
                            break;
                        case "_type:":
                            type = value;
                            break;
                        case "_text:":
                            text = value;
                            break;
                        case "_separator:":
                            separator = value;
                            break;
                        case "_round:":
                            round = Integer.parseInt(value);
                            break;
                    }
                }
                Random random = new Random();
                if (seed!=0){
                    random = new Random(seed);
                }
                if (Objects.equals(type, "string")){
                    String[] splitRandom = text.split(separator);
                    return splitRandom[random.nextInt(splitRandom.length)];
                } else if (type.equalsIgnoreCase("int")){
                    return String.valueOf(random.nextInt((int) (max - min + 1)) + (int)min);
                } else if (type.equalsIgnoreCase("double")){
                    return String.format("%." + round + "f", random.nextDouble((max - min)) + min);
                } else {
                    return null;
                }
        }


        return super.onRequest(player, args);
    }
}
