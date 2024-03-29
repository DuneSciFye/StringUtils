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
                return "Incorrect argument amount. Input: " + length;
            case "randomint":
                arguments = "_" + arguments;
                if (!arguments.contains("_min:")||!arguments.contains("_max:")){
                    return "Invalid arguments.";
                }
                String regex = "(_seed:|_min:|_max:)(.+?)(?=(_seed:|_min:|_max:|$))";

                Pattern pattern = Pattern.compile(regex);
                Matcher matcher = pattern.matcher(arguments);

                int min = 0, max = 0;
                long seedInt = 0;

                while (matcher.find()) {
                    String key = matcher.group(1);
                    String value = matcher.group(2);

                    switch (key) {
                        case "_seed:":
                            seedInt = Long.parseLong(value);
                            break;
                        case "_min:":
                            min = Integer.parseInt(value);
                            break;
                        case "_max:":
                            max = Integer.parseInt(value);
                            break;
                    }
                }
                Random random = new Random();
                if (seedInt!=0){
                    random = new Random(seedInt);
                }
                return String.valueOf(random.nextInt((max - min + 1)) + min);
            case "randomdouble":
                arguments = "_" + arguments;
                if (!arguments.contains("_min:")||!arguments.contains("_max:")){
                    return "Invalid arguments. Missing min or max.";
                }
                String regexDouble = "(_seed:|_round:|_min:|_max:)(.+?)(?=(_seed:|_round:|_min:|_max:|$))";

                Pattern patternDouble = Pattern.compile(regexDouble);
                Matcher matcherDouble = patternDouble.matcher(arguments);

                double minDouble = 0, maxDouble = 0;
                long seedDouble = 0;
                int round = 2;

                while (matcherDouble.find()) {
                    String key = matcherDouble.group(1);
                    String value = matcherDouble.group(2);

                    switch (key) {
                        case "_seed:":
                            seedDouble = Long.parseLong(value);
                            break;
                        case "_min:":
                            minDouble = Double.parseDouble(value);
                            break;
                        case "_max:":
                            maxDouble = Double.parseDouble(value);
                            break;
                        case "_round:":
                            round = Integer.parseInt(value);
                            break;
                    }
                }
                Random randomDouble = new Random();
                if (seedDouble!=0){
                    randomDouble = new Random(seedDouble);
                }
                return String.format("%." + round + "f", randomDouble.nextDouble((maxDouble - minDouble)) + minDouble);
            case "randomstring":
                arguments = "_" + arguments;
                if (!arguments.contains("_text:")){
                    return "Invalid arguments. Missing text.";
                }
                String regexString = "(_seed:|_text:|_separator:)(.+?)(?=(_seed:|_text:|_separator:|$))";

                Pattern patternString = Pattern.compile(regexString);
                Matcher matcherString = patternString.matcher(arguments);

                String text = null, separator = ",";
                long seedString = 0;

                while (matcherString.find()) {
                    String key = matcherString.group(1);
                    String value = matcherString.group(2);

                    switch (key) {
                        case "_seed:":
                            seedString = Long.parseLong(value);
                            break;
                        case "_text:":
                            text = value;
                            break;
                        case "_separator:":
                            separator = value;
                            break;
                    }
                }
                Random randomString = new Random();
                if (seedString!=0){
                    randomString = new Random(seedString);
                }
                String[] splitRandom = text.split(separator);
                return splitRandom[randomString.nextInt(splitRandom.length)];
            case "changecolor":
                arguments = "_" + arguments;
                if (!arguments.contains("_input:")||!arguments.contains("_newcolor:")){
                    return "Invalid arguments. Requires input and newcolor.";
                }
                String regexChangeColor = "(_input:|_newcolor:)(.+?)(?=(_input:|_newcolor:|$))";

                Pattern patternChangeColor = Pattern.compile(regexChangeColor);
                Matcher matcherChangeCOlor = patternChangeColor.matcher(arguments);

                String input = "", newcolor = "";

                while (matcherChangeCOlor.find()) {
                    String key = matcherChangeCOlor.group(1);
                    String value = matcherChangeCOlor.group(2);

                    switch (key) {
                        case "_input:":
                            input = value;
                            break;
                        case "_newcolor:":
                            newcolor = value;
                            break;
                    }
                }
                if (input.contains("_CONCRETE_POWDER")){
                    return newcolor + input.substring(input.length() - 16);
                } else if (input.contains("_CONCRETE")){
                    return newcolor + input.substring(input.length() - 9);
                } else if (input.contains("_STAINED_GLASS_PANE")){
                    return newcolor + input.substring(input.length() - 19);
                } else if (input.contains("_WOOL")){
                    return newcolor + input.substring(input.length() - 5);
                } else if (input.contains("_GlAZED_TERRACOTTA")){
                    return newcolor + input.substring(input.length() - 18);
                } else if (input.contains("_TERRACOTTA")){
                    return newcolor + input.substring(input.length() - 11);
                } else if (input.contains("_STAINED_GLASS")){
                    return newcolor + input.substring(input.length() - 14);
                } else if (input.contains("_CARPET")){
                    return newcolor + input.substring(input.length() - 7);
                }

        }


        return super.onRequest(player, args);
    }
}
