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

            case "replace":
                arguments = "_" + arguments;
                if (!arguments.contains("_text:")||!arguments.contains("_from:")||!arguments.contains("_to:")){
                    return "Invalid arguments. Requires text, from, and to.";
                }
                String regexChangeColor = "(_text:|_from:|_to:)(.+?)(?=(_text:|_from:|_to:|$))";

                Pattern patternChangeColor = Pattern.compile(regexChangeColor);
                Matcher matcherChangeColor = patternChangeColor.matcher(arguments);

                String textReplace = "", oldChar = "", newChar = "";

                while (matcherChangeColor.find()) {
                    String key = matcherChangeColor.group(1);
                    String value = matcherChangeColor.group(2);

                    switch (key) {
                        case "_text:":
                            textReplace = value;
                            break;
                        case "_from:":
                            oldChar = value;
                            break;
                        case "_to:":
                            newChar = value;
                            break;
                    }
                }
                return textReplace.replace(oldChar, newChar);

        }


        return super.onRequest(player, args);
    }
}
