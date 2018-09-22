package com.epam.dojo.icancode;

/*-
 * #%L
 * iCanCode - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2016 EPAM
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */


import com.codenjoy.dojo.client.LocalGameRunner;
import com.codenjoy.dojo.services.Dice;
import com.epam.dojo.icancode.client.Board;
import com.epam.dojo.icancode.client.ai.AISolver;
import com.epam.dojo.icancode.model.interfaces.ILevel;
import com.epam.dojo.icancode.services.GameRunner;
import com.epam.dojo.icancode.services.Levels;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class SmokeTest {
    @Test
    public void test() {
        // given
        List<String> messages = new LinkedList<>();

        LocalGameRunner.timeout = 0;
        LocalGameRunner.out = (e) -> messages.add(e);
        LocalGameRunner.countIterations = 15;

        Dice dice = LocalGameRunner.getDice(
                1, 2, 3, 0, 1, 2, 3, 2, // some random numbers
                0, 3, 2, 1, 2, 3, 2, 1,
                2, 3, 2, 1, 2, 3, 1, 2,
                1, 2, 0, 3, 2, 2, 1, 3,
                1, 1, 2, 3, 1, 2, 3, 2,
                1, 2, 3, 2, 3, 2, 1, 2,
                1, 2, 3, 2, 1, 2, 3, 0);

        GameRunner gameType = new GameRunner() {
            @Override
            public Dice getDice() {
                return dice;
            }

            @Override
            public ILevel loadLevel(int level) {
                return Levels.load(
                        "                " +
                        " ############## " +
                        " #S...O.....˅.# " +
                        " #˃.....$O....# " +
                        " #.B..####.B..# " +
                        " #...Z#  #Z...# " +
                        " #.O###  ###.O# " +
                        " #.$#      #..# " +
                        " #..#      #$.# " +
                        " #O.###  ###O.# " +
                        " #...Z#  #Z...# " +
                        " #.B..####.B..# " +
                        " #....O$.....˂# " +
                        " #.˄.....O...E# " +
                        " ############## " +
                        "                ");
            }
        };

        // when
        LocalGameRunner.run(gameType,
                new AISolver(null),
                new Board());

        // then
        assertEquals("DICE:1\n" +
                        "DICE_CORRECTED < 1 :0\n" +
                        "1: Layer1                 Layer2\n" +
                        "1:  01234567890123456789   01234567890123456789\n" +
                        "1:19                     19                     Robots: [4,15],\n" +
                        "1:18                     18                     Gold: [5,10], [9,5], [10,14], [14,9]\n" +
                        "1:17                     17                     Starts: [4,15]\n" +
                        "1:16   ╔════════════┐    16   ╔════════════┐    Exits: [15,4]\n" +
                        "1:15   ║S...O.....˅.│    15   ║☺-----------│    Boxes: [5,6], [5,13], [13,6], [13,13]\n" +
                        "1:14   ║˃.....$O....│    14   ║------------│    Holes: [4,8], [5,11], [8,5], [8,15], [11,4], [11,14], [14,8], [15,11]\n" +
                        "1:13   ║....┌──╗....│    13   ║-B--┌──╗-B--│    LaserMachine: [4,14], [5,4], [14,15], [15,5]\n" +
                        "1:12   ║...Z│  ║Z...│    12   ║----│  ║----│    Lasers: \n" +
                        "1:11   ║.O┌─┘  └─╗.O│    11   ║--┌─┘  └─╗--│   \n" +
                        "1:10   ║.$│      ║..│    10   ║--│      ║--│   \n" +
                        "1: 9   ║..│      ║$.│     9   ║--│      ║--│   \n" +
                        "1: 8   ║O.╚═┐  ╔═╝O.│     8   ║--╚═┐  ╔═╝--│   \n" +
                        "1: 7   ║...Z│  ║Z...│     7   ║----│  ║----│   \n" +
                        "1: 6   ║....╚══╝....│     6   ║-B--╚══╝-B--│   \n" +
                        "1: 5   ║....O$.....˂│     5   ║------------│   \n" +
                        "1: 4   ║.˄.....O...E│     4   ║------------│   \n" +
                        "1: 3   └────────────┘     3   └────────────┘   \n" +
                        "1: 2                      2                    \n" +
                        "1: 1                      1                    \n" +
                        "1: 0                      0                    \n" +
                        "1:  01234567890123456789   01234567890123456789\n" +
                        "1:Answer: RIGHT\n" +
                        "------------------------------------------\n" +
                        "1: Layer1                 Layer2\n" +
                        "1:  01234567890123456789   01234567890123456789\n" +
                        "1:19                     19                     Robots: [5,15],\n" +
                        "1:18                     18                     Gold: [5,10], [9,5], [10,14], [14,9]\n" +
                        "1:17                     17                     Starts: [4,15]\n" +
                        "1:16   ╔════════════┐    16   ╔════════════┐    Exits: [15,4]\n" +
                        "1:15   ║S...O.....˅.│    15   ║-☺----------│    Boxes: [5,6], [5,13], [13,6], [13,13]\n" +
                        "1:14   ║˃.....$O....│    14   ║------------│    Holes: [4,8], [5,11], [8,5], [8,15], [11,4], [11,14], [14,8], [15,11]\n" +
                        "1:13   ║....┌──╗....│    13   ║-B--┌──╗-B--│    LaserMachine: [4,14], [5,4], [14,15], [15,5]\n" +
                        "1:12   ║...Z│  ║Z...│    12   ║----│  ║----│    Lasers: \n" +
                        "1:11   ║.O┌─┘  └─╗.O│    11   ║--┌─┘  └─╗--│   \n" +
                        "1:10   ║.$│      ║..│    10   ║--│      ║--│   \n" +
                        "1: 9   ║..│      ║$.│     9   ║--│      ║--│   \n" +
                        "1: 8   ║O.╚═┐  ╔═╝O.│     8   ║--╚═┐  ╔═╝--│   \n" +
                        "1: 7   ║...Z│  ║Z...│     7   ║----│  ║----│   \n" +
                        "1: 6   ║....╚══╝....│     6   ║-B--╚══╝-B--│   \n" +
                        "1: 5   ║....O$.....˂│     5   ║------------│   \n" +
                        "1: 4   ║.˄.....O...E│     4   ║------------│   \n" +
                        "1: 3   └────────────┘     3   └────────────┘   \n" +
                        "1: 2                      2                    \n" +
                        "1: 1                      1                    \n" +
                        "1: 0                      0                    \n" +
                        "1:  01234567890123456789   01234567890123456789\n" +
                        "1:Answer: DOWN\n" +
                        "------------------------------------------\n" +
                        "1: Layer1                 Layer2\n" +
                        "1:  01234567890123456789   01234567890123456789\n" +
                        "1:19                     19                     Robots: [5,14],\n" +
                        "1:18                     18                     Gold: [5,10], [9,5], [10,14], [14,9]\n" +
                        "1:17                     17                     Starts: [4,15]\n" +
                        "1:16   ╔════════════┐    16   ╔════════════┐    Exits: [15,4]\n" +
                        "1:15   ║S...O.....˅.│    15   ║------------│    Boxes: [5,6], [5,13], [13,6], [13,13]\n" +
                        "1:14   ║˃.....$O....│    14   ║-☺----------│    Holes: [4,8], [5,11], [8,5], [8,15], [11,4], [11,14], [14,8], [15,11]\n" +
                        "1:13   ║....┌──╗....│    13   ║-B--┌──╗-B--│    LaserMachine: [4,14], [5,4], [14,15], [15,5]\n" +
                        "1:12   ║...Z│  ║Z...│    12   ║----│  ║----│    Lasers: \n" +
                        "1:11   ║.O┌─┘  └─╗.O│    11   ║--┌─┘  └─╗--│   \n" +
                        "1:10   ║.$│      ║..│    10   ║--│      ║--│   \n" +
                        "1: 9   ║..│      ║$.│     9   ║--│      ║--│   \n" +
                        "1: 8   ║O.╚═┐  ╔═╝O.│     8   ║--╚═┐  ╔═╝--│   \n" +
                        "1: 7   ║...Z│  ║Z...│     7   ║----│  ║----│   \n" +
                        "1: 6   ║....╚══╝....│     6   ║-B--╚══╝-B--│   \n" +
                        "1: 5   ║....O$.....˂│     5   ║------------│   \n" +
                        "1: 4   ║.˄.....O...E│     4   ║------------│   \n" +
                        "1: 3   └────────────┘     3   └────────────┘   \n" +
                        "1: 2                      2                    \n" +
                        "1: 1                      1                    \n" +
                        "1: 0                      0                    \n" +
                        "1:  01234567890123456789   01234567890123456789\n" +
                        "1:Answer: RIGHT\n" +
                        "------------------------------------------\n" +
                        "1: Layer1                 Layer2\n" +
                        "1:  01234567890123456789   01234567890123456789\n" +
                        "1:19                     19                     Robots: [6,14],\n" +
                        "1:18                     18                     Gold: [5,10], [9,5], [10,14], [14,9]\n" +
                        "1:17                     17                     Starts: [4,15]\n" +
                        "1:16   ╔════════════┐    16   ╔════════════┐    Exits: [15,4]\n" +
                        "1:15   ║S...O.....˅.│    15   ║------------│    Boxes: [5,6], [5,13], [13,6], [13,13]\n" +
                        "1:14   ║˃.....$O....│    14   ║--☺---------│    Holes: [4,8], [5,11], [8,5], [8,15], [11,4], [11,14], [14,8], [15,11]\n" +
                        "1:13   ║....┌──╗....│    13   ║-B--┌──╗-B--│    LaserMachine: [4,14], [5,4], [14,15], [15,5]\n" +
                        "1:12   ║...Z│  ║Z...│    12   ║----│  ║----│    Lasers: \n" +
                        "1:11   ║.O┌─┘  └─╗.O│    11   ║--┌─┘  └─╗--│   \n" +
                        "1:10   ║.$│      ║..│    10   ║--│      ║--│   \n" +
                        "1: 9   ║..│      ║$.│     9   ║--│      ║--│   \n" +
                        "1: 8   ║O.╚═┐  ╔═╝O.│     8   ║--╚═┐  ╔═╝--│   \n" +
                        "1: 7   ║...Z│  ║Z...│     7   ║----│  ║----│   \n" +
                        "1: 6   ║....╚══╝....│     6   ║-B--╚══╝-B--│   \n" +
                        "1: 5   ║....O$.....˂│     5   ║------------│   \n" +
                        "1: 4   ║.˄.....O...E│     4   ║------------│   \n" +
                        "1: 3   └────────────┘     3   └────────────┘   \n" +
                        "1: 2                      2                    \n" +
                        "1: 1                      1                    \n" +
                        "1: 0                      0                    \n" +
                        "1:  01234567890123456789   01234567890123456789\n" +
                        "1:Answer: RIGHT\n" +
                        "------------------------------------------\n" +
                        "1: Layer1                 Layer2\n" +
                        "1:  01234567890123456789   01234567890123456789\n" +
                        "1:19                     19                     Robots: [7,14],\n" +
                        "1:18                     18                     Gold: [5,10], [9,5], [10,14], [14,9]\n" +
                        "1:17                     17                     Starts: [4,15]\n" +
                        "1:16   ╔════════════┐    16   ╔════════════┐    Exits: [15,4]\n" +
                        "1:15   ║S...O.....˅.│    15   ║------------│    Boxes: [5,6], [5,13], [13,6], [13,13]\n" +
                        "1:14   ║˃.....$O....│    14   ║---☺--------│    Holes: [4,8], [5,11], [8,5], [8,15], [11,4], [11,14], [14,8], [15,11]\n" +
                        "1:13   ║....┌──╗....│    13   ║-B--┌──╗-B--│    LaserMachine: [4,14], [5,4], [14,15], [15,5]\n" +
                        "1:12   ║...Z│  ║Z...│    12   ║----│  ║----│    Lasers: \n" +
                        "1:11   ║.O┌─┘  └─╗.O│    11   ║--┌─┘  └─╗--│   \n" +
                        "1:10   ║.$│      ║..│    10   ║--│      ║--│   \n" +
                        "1: 9   ║..│      ║$.│     9   ║--│      ║--│   \n" +
                        "1: 8   ║O.╚═┐  ╔═╝O.│     8   ║--╚═┐  ╔═╝--│   \n" +
                        "1: 7   ║...Z│  ║Z...│     7   ║----│  ║----│   \n" +
                        "1: 6   ║....╚══╝....│     6   ║-B--╚══╝-B--│   \n" +
                        "1: 5   ║....O$.....˂│     5   ║------------│   \n" +
                        "1: 4   ║.˄.....O...E│     4   ║------------│   \n" +
                        "1: 3   └────────────┘     3   └────────────┘   \n" +
                        "1: 2                      2                    \n" +
                        "1: 1                      1                    \n" +
                        "1: 0                      0                    \n" +
                        "1:  01234567890123456789   01234567890123456789\n" +
                        "1:Answer: RIGHT\n" +
                        "------------------------------------------\n" +
                        "1: Layer1                 Layer2\n" +
                        "1:  01234567890123456789   01234567890123456789\n" +
                        "1:19                     19                     Robots: [8,14],\n" +
                        "1:18                     18                     Gold: [5,10], [9,5], [10,14], [14,9]\n" +
                        "1:17                     17                     Starts: [4,15]\n" +
                        "1:16   ╔════════════┐    16   ╔════════════┐    Exits: [15,4]\n" +
                        "1:15   ║S...O.....˅.│    15   ║------------│    Boxes: [5,6], [5,13], [13,6], [13,13]\n" +
                        "1:14   ║˃.....$O....│    14   ║----☺-------│    Holes: [4,8], [5,11], [8,5], [8,15], [11,4], [11,14], [14,8], [15,11]\n" +
                        "1:13   ║....┌──╗....│    13   ║-B--┌──╗-B--│    LaserMachine: [4,14], [5,4], [14,15], [15,5]\n" +
                        "1:12   ║...Z│  ║Z...│    12   ║----│  ║----│    Lasers: \n" +
                        "1:11   ║.O┌─┘  └─╗.O│    11   ║--┌─┘  └─╗--│   \n" +
                        "1:10   ║.$│      ║..│    10   ║--│      ║--│   \n" +
                        "1: 9   ║..│      ║$.│     9   ║--│      ║--│   \n" +
                        "1: 8   ║O.╚═┐  ╔═╝O.│     8   ║--╚═┐  ╔═╝--│   \n" +
                        "1: 7   ║...Z│  ║Z...│     7   ║----│  ║----│   \n" +
                        "1: 6   ║....╚══╝....│     6   ║-B--╚══╝-B--│   \n" +
                        "1: 5   ║....O$.....˂│     5   ║------------│   \n" +
                        "1: 4   ║.˄.....O...E│     4   ║------------│   \n" +
                        "1: 3   └────────────┘     3   └────────────┘   \n" +
                        "1: 2                      2                    \n" +
                        "1: 1                      1                    \n" +
                        "1: 0                      0                    \n" +
                        "1:  01234567890123456789   01234567890123456789\n" +
                        "1:Answer: RIGHT\n" +
                        "------------------------------------------\n" +
                        "1: Layer1                 Layer2\n" +
                        "1:  01234567890123456789   01234567890123456789\n" +
                        "1:19                     19                     Robots: [9,14],\n" +
                        "1:18                     18                     Gold: [5,10], [9,5], [10,14], [14,9]\n" +
                        "1:17                     17                     Starts: [4,15]\n" +
                        "1:16   ╔════════════┐    16   ╔════════════┐    Exits: [15,4]\n" +
                        "1:15   ║S...O.....▼.│    15   ║------------│    Boxes: [5,6], [5,13], [13,6], [13,13]\n" +
                        "1:14   ║►.....$O....│    14   ║-----☺------│    Holes: [4,8], [5,11], [8,5], [8,15], [11,4], [11,14], [14,8], [15,11]\n" +
                        "1:13   ║....┌──╗....│    13   ║-B--┌──╗-B--│    LaserMachine: [4,14], [5,4], [14,15], [15,5]\n" +
                        "1:12   ║...Z│  ║Z...│    12   ║----│  ║----│    Lasers: \n" +
                        "1:11   ║.O┌─┘  └─╗.O│    11   ║--┌─┘  └─╗--│   \n" +
                        "1:10   ║.$│      ║..│    10   ║--│      ║--│   \n" +
                        "1: 9   ║..│      ║$.│     9   ║--│      ║--│   \n" +
                        "1: 8   ║O.╚═┐  ╔═╝O.│     8   ║--╚═┐  ╔═╝--│   \n" +
                        "1: 7   ║...Z│  ║Z...│     7   ║----│  ║----│   \n" +
                        "1: 6   ║....╚══╝....│     6   ║-B--╚══╝-B--│   \n" +
                        "1: 5   ║....O$.....◄│     5   ║------------│   \n" +
                        "1: 4   ║.▲.....O...E│     4   ║------------│   \n" +
                        "1: 3   └────────────┘     3   └────────────┘   \n" +
                        "1: 2                      2                    \n" +
                        "1: 1                      1                    \n" +
                        "1: 0                      0                    \n" +
                        "1:  01234567890123456789   01234567890123456789\n" +
                        "1:Answer: RIGHT\n" +
                        "------------------------------------------\n" +
                        "1: Layer1                 Layer2\n" +
                        "1:  01234567890123456789   01234567890123456789\n" +
                        "1:19                     19                     Robots: [10,14],\n" +
                        "1:18                     18                     Gold: [5,10], [9,5], [14,9]\n" +
                        "1:17                     17                     Starts: [4,15]\n" +
                        "1:16   ╔════════════┐    16   ╔════════════┐    Exits: [15,4]\n" +
                        "1:15   ║S...O.....˅.│    15   ║------------│    Boxes: [5,6], [5,13], [13,6], [13,13]\n" +
                        "1:14   ║˃......O....│    14   ║-→----☺---↓-│    Holes: [4,8], [5,11], [8,5], [8,15], [11,4], [11,14], [14,8], [15,11]\n" +
                        "1:13   ║....┌──╗....│    13   ║-B--┌──╗-B--│    LaserMachine: [4,14], [5,4], [14,15], [15,5]\n" +
                        "1:12   ║...Z│  ║Z...│    12   ║----│  ║----│    Lasers: [5,5], [5,14], [14,5], [14,14]\n" +
                        "1:11   ║.O┌─┘  └─╗.O│    11   ║--┌─┘  └─╗--│   \n" +
                        "1:10   ║.$│      ║..│    10   ║--│      ║--│   \n" +
                        "1: 9   ║..│      ║$.│     9   ║--│      ║--│   \n" +
                        "1: 8   ║O.╚═┐  ╔═╝O.│     8   ║--╚═┐  ╔═╝--│   \n" +
                        "1: 7   ║...Z│  ║Z...│     7   ║----│  ║----│   \n" +
                        "1: 6   ║....╚══╝....│     6   ║-B--╚══╝-B--│   \n" +
                        "1: 5   ║....O$.....˂│     5   ║-↑--------←-│   \n" +
                        "1: 4   ║.˄.....O...E│     4   ║------------│   \n" +
                        "1: 3   └────────────┘     3   └────────────┘   \n" +
                        "1: 2                      2                    \n" +
                        "1: 1                      1                    \n" +
                        "1: 0                      0                    \n" +
                        "1:  01234567890123456789   01234567890123456789\n" +
                        "1:Answer: LEFT\n" +
                        "------------------------------------------\n" +
                        "1: Layer1                 Layer2\n" +
                        "1:  01234567890123456789   01234567890123456789\n" +
                        "1:19                     19                     Robots: [9,14],\n" +
                        "1:18                     18                     Gold: [5,10], [9,5], [14,9]\n" +
                        "1:17                     17                     Starts: [4,15]\n" +
                        "1:16   ╔════════════┐    16   ╔════════════┐    Exits: [15,4]\n" +
                        "1:15   ║S...O.....˅.│    15   ║------------│    Boxes: [5,6], [5,13], [13,6], [13,13]\n" +
                        "1:14   ║˃......O....│    14   ║--→--☺------│    Holes: [4,8], [5,11], [8,5], [8,15], [11,4], [11,14], [14,8], [15,11]\n" +
                        "1:13   ║....┌──╗....│    13   ║-B--┌──╗-B↓-│    LaserMachine: [4,14], [5,4], [14,15], [15,5]\n" +
                        "1:12   ║...Z│  ║Z...│    12   ║----│  ║----│    Lasers: [6,14], [13,5], [14,13]\n" +
                        "1:11   ║.O┌─┘  └─╗.O│    11   ║--┌─┘  └─╗--│   \n" +
                        "1:10   ║.$│      ║..│    10   ║--│      ║--│   \n" +
                        "1: 9   ║..│      ║$.│     9   ║--│      ║--│   \n" +
                        "1: 8   ║O.╚═┐  ╔═╝O.│     8   ║--╚═┐  ╔═╝--│   \n" +
                        "1: 7   ║...Z│  ║Z...│     7   ║----│  ║----│   \n" +
                        "1: 6   ║....╚══╝....│     6   ║-B--╚══╝-B--│   \n" +
                        "1: 5   ║....O$.....˂│     5   ║---------←--│   \n" +
                        "1: 4   ║.˄.....O...E│     4   ║------------│   \n" +
                        "1: 3   └────────────┘     3   └────────────┘   \n" +
                        "1: 2                      2                    \n" +
                        "1: 1                      1                    \n" +
                        "1: 0                      0                    \n" +
                        "1:  01234567890123456789   01234567890123456789\n" +
                        "1:Answer: LEFT\n" +
                        "------------------------------------------\n" +
                        "1: Layer1                 Layer2\n" +
                        "1:  01234567890123456789   01234567890123456789\n" +
                        "1:19                     19                     Robots: [8,14],\n" +
                        "1:18                     18                     Gold: [5,10], [9,5], [14,9]\n" +
                        "1:17                     17                     Starts: [4,15]\n" +
                        "1:16   ╔════════════┐    16   ╔════════════┐    Exits: [15,4]\n" +
                        "1:15   ║S...O.....˅.│    15   ║------------│    Boxes: [5,6], [5,13], [13,6], [13,13]\n" +
                        "1:14   ║˃......O....│    14   ║---→☺-------│    Holes: [4,8], [5,11], [8,5], [8,15], [11,4], [11,14], [14,8], [15,11]\n" +
                        "1:13   ║....┌──╗....│    13   ║-B--┌──╗-B--│    LaserMachine: [4,14], [5,4], [14,15], [15,5]\n" +
                        "1:12   ║...Z│  ║Z...│    12   ║----│  ║--↓-│    Lasers: [7,14], [12,5], [14,12]\n" +
                        "1:11   ║.O┌─┘  └─╗.O│    11   ║--┌─┘  └─╗--│   \n" +
                        "1:10   ║.$│      ║..│    10   ║--│      ║--│   \n" +
                        "1: 9   ║..│      ║$.│     9   ║--│      ║--│   \n" +
                        "1: 8   ║O.╚═┐  ╔═╝O.│     8   ║--╚═┐  ╔═╝--│   \n" +
                        "1: 7   ║...Z│  ║Z...│     7   ║----│  ║----│   \n" +
                        "1: 6   ║....╚══╝....│     6   ║-B--╚══╝-B--│   \n" +
                        "1: 5   ║....O$.....˂│     5   ║--------←---│   \n" +
                        "1: 4   ║.˄.....O...E│     4   ║------------│   \n" +
                        "1: 3   └────────────┘     3   └────────────┘   \n" +
                        "1: 2                      2                    \n" +
                        "1: 1                      1                    \n" +
                        "1: 0                      0                    \n" +
                        "1:  01234567890123456789   01234567890123456789\n" +
                        "1:Answer: ACT(1),LEFT\n" +
                        "------------------------------------------\n" +
                        "1: Layer1                 Layer2\n" +
                        "1:  01234567890123456789   01234567890123456789\n" +
                        "1:19                     19                     Robots: [7,14],\n" +
                        "1:18                     18                     Gold: [5,10], [9,5], [14,9]\n" +
                        "1:17                     17                     Starts: [4,15]\n" +
                        "1:16   ╔════════════┐    16   ╔════════════┐    Exits: [15,4]\n" +
                        "1:15   ║S...O.....˅.│    15   ║------------│    Boxes: [5,6], [5,13], [13,6], [13,13]\n" +
                        "1:14   ║˃......O....│    14   ║---*→-------│    Holes: [4,8], [5,11], [8,5], [8,15], [11,4], [11,14], [14,8], [15,11]\n" +
                        "1:13   ║....┌──╗....│    13   ║-B--┌──╗-B--│    LaserMachine: [4,14], [5,4], [14,15], [15,5]\n" +
                        "1:12   ║...Z│  ║Z...│    12   ║----│  ║----│    Lasers: [8,14], [11,5], [14,11]\n" +
                        "1:11   ║.O┌─┘  └─╗.O│    11   ║--┌─┘  └─╗↓-│   \n" +
                        "1:10   ║.$│      ║..│    10   ║--│      ║--│   \n" +
                        "1: 9   ║..│      ║$.│     9   ║--│      ║--│   \n" +
                        "1: 8   ║O.╚═┐  ╔═╝O.│     8   ║--╚═┐  ╔═╝--│   \n" +
                        "1: 7   ║...Z│  ║Z...│     7   ║----│  ║----│   \n" +
                        "1: 6   ║....╚══╝....│     6   ║-B--╚══╝-B--│   \n" +
                        "1: 5   ║....O$.....˂│     5   ║-------←----│   \n" +
                        "1: 4   ║.˄.....O...E│     4   ║------------│   \n" +
                        "1: 3   └────────────┘     3   └────────────┘   \n" +
                        "1: 2                      2                    \n" +
                        "1: 1                      1                    \n" +
                        "1: 0                      0                    \n" +
                        "1:  01234567890123456789   01234567890123456789\n" +
                        "1:Answer: DOWN\n" +
                        "------------------------------------------\n" +
                        "1: Layer1                 Layer2\n" +
                        "1:  01234567890123456789   01234567890123456789\n" +
                        "1:19                     19                     Robots: [6,14],\n" +
                        "1:18                     18                     Gold: [5,10], [9,5], [14,9]\n" +
                        "1:17                     17                     Starts: [4,15]\n" +
                        "1:16   ╔════════════┐    16   ╔════════════┐    Exits: [15,4]\n" +
                        "1:15   ║S...O.....˅.│    15   ║------------│    Boxes: [5,6], [5,13], [13,6], [13,13]\n" +
                        "1:14   ║˃......O....│    14   ║--☺--→------│    Holes: [4,8], [5,11], [8,5], [8,15], [11,4], [11,14], [14,8], [15,11]\n" +
                        "1:13   ║....┌──╗....│    13   ║-B--┌──╗-B--│    LaserMachine: [4,14], [5,4], [14,15], [15,5]\n" +
                        "1:12   ║...Z│  ║Z...│    12   ║----│  ║----│    Lasers: [9,14], [10,5], [14,10]\n" +
                        "1:11   ║.O┌─┘  └─╗.O│    11   ║--┌─┘  └─╗--│   \n" +
                        "1:10   ║.$│      ║..│    10   ║--│      ║↓-│   \n" +
                        "1: 9   ║..│      ║$.│     9   ║--│      ║--│   \n" +
                        "1: 8   ║O.╚═┐  ╔═╝O.│     8   ║--╚═┐  ╔═╝--│   \n" +
                        "1: 7   ║...Z│  ║Z...│     7   ║----│  ║----│   \n" +
                        "1: 6   ║....╚══╝....│     6   ║-B--╚══╝-B--│   \n" +
                        "1: 5   ║....O$.....˂│     5   ║------←-----│   \n" +
                        "1: 4   ║.˄.....O...E│     4   ║------------│   \n" +
                        "1: 3   └────────────┘     3   └────────────┘   \n" +
                        "1: 2                      2                    \n" +
                        "1: 1                      1                    \n" +
                        "1: 0                      0                    \n" +
                        "1:  01234567890123456789   01234567890123456789\n" +
                        "1:Answer: DOWN\n" +
                        "------------------------------------------\n" +
                        "1: Layer1                 Layer2\n" +
                        "1:  01234567890123456789   01234567890123456789\n" +
                        "1:19                     19                     Robots: [6,13],\n" +
                        "1:18                     18                     Gold: [5,10], [9,5], [14,9]\n" +
                        "1:17                     17                     Starts: [4,15]\n" +
                        "1:16   ╔════════════┐    16   ╔════════════┐    Exits: [15,4]\n" +
                        "1:15   ║S...O.....˅.│    15   ║------------│    Boxes: [5,6], [5,13], [13,6], [13,13]\n" +
                        "1:14   ║˃......O....│    14   ║------→-----│    Holes: [4,8], [5,11], [8,5], [8,15], [11,4], [11,14], [14,8], [15,11]\n" +
                        "1:13   ║....┌──╗....│    13   ║-B☺-┌──╗-B--│    LaserMachine: [4,14], [5,4], [14,15], [15,5]\n" +
                        "1:12   ║...Z│  ║Z...│    12   ║----│  ║----│    Lasers: [9,5], [10,14], [14,9]\n" +
                        "1:11   ║.O┌─┘  └─╗.O│    11   ║--┌─┘  └─╗--│   \n" +
                        "1:10   ║.$│      ║..│    10   ║--│      ║--│   \n" +
                        "1: 9   ║..│      ║$.│     9   ║--│      ║↓-│   \n" +
                        "1: 8   ║O.╚═┐  ╔═╝O.│     8   ║--╚═┐  ╔═╝--│   \n" +
                        "1: 7   ║...Z│  ║Z...│     7   ║----│  ║----│   \n" +
                        "1: 6   ║....╚══╝....│     6   ║-B--╚══╝-B--│   \n" +
                        "1: 5   ║....O$.....˂│     5   ║-----←------│   \n" +
                        "1: 4   ║.˄.....O...E│     4   ║------------│   \n" +
                        "1: 3   └────────────┘     3   └────────────┘   \n" +
                        "1: 2                      2                    \n" +
                        "1: 1                      1                    \n" +
                        "1: 0                      0                    \n" +
                        "1:  01234567890123456789   01234567890123456789\n" +
                        "1:Answer: DOWN\n" +
                        "------------------------------------------\n" +
                        "1: Layer1                 Layer2\n" +
                        "1:  01234567890123456789   01234567890123456789\n" +
                        "1:19                     19                     Robots: [6,12],\n" +
                        "1:18                     18                     Gold: [5,10], [9,5], [14,9]\n" +
                        "1:17                     17                     Starts: [4,15]\n" +
                        "1:16   ╔════════════┐    16   ╔════════════┐    Exits: [15,4]\n" +
                        "1:15   ║S...O.....▼.│    15   ║------------│    Boxes: [5,6], [5,13], [13,6], [13,13]\n" +
                        "1:14   ║►......O....│    14   ║-------→----│    Holes: [4,8], [5,11], [8,5], [8,15], [11,4], [11,14], [14,8], [15,11]\n" +
                        "1:13   ║....┌──╗....│    13   ║-B--┌──╗-B--│    LaserMachine: [4,14], [5,4], [14,15], [15,5]\n" +
                        "1:12   ║...Z│  ║Z...│    12   ║--☺-│  ║----│    Lasers: [8,5], [11,14], [14,8]\n" +
                        "1:11   ║.O┌─┘  └─╗.O│    11   ║--┌─┘  └─╗--│   \n" +
                        "1:10   ║.$│      ║..│    10   ║--│      ║--│   \n" +
                        "1: 9   ║..│      ║$.│     9   ║--│      ║--│   \n" +
                        "1: 8   ║O.╚═┐  ╔═╝O.│     8   ║--╚═┐  ╔═╝↓-│   \n" +
                        "1: 7   ║...Z│  ║Z...│     7   ║----│  ║----│   \n" +
                        "1: 6   ║....╚══╝....│     6   ║-B--╚══╝-B--│   \n" +
                        "1: 5   ║....O$.....◄│     5   ║----←-------│   \n" +
                        "1: 4   ║.▲.....O...E│     4   ║------------│   \n" +
                        "1: 3   └────────────┘     3   └────────────┘   \n" +
                        "1: 2                      2                    \n" +
                        "1: 1                      1                    \n" +
                        "1: 0                      0                    \n" +
                        "1:  01234567890123456789   01234567890123456789\n" +
                        "1:Answer: LEFT\n" +
                        "------------------------------------------\n" +
                        "1: Layer1                 Layer2\n" +
                        "1:  01234567890123456789   01234567890123456789\n" +
                        "1:19                     19                     Robots: [5,12],\n" +
                        "1:18                     18                     Gold: [5,10], [9,5], [14,9]\n" +
                        "1:17                     17                     Starts: [4,15]\n" +
                        "1:16   ╔════════════┐    16   ╔════════════┐    Exits: [15,4]\n" +
                        "1:15   ║S...O.....˅.│    15   ║------------│    Boxes: [5,6], [5,13], [13,6], [13,13]\n" +
                        "1:14   ║˃......O....│    14   ║-→------→-↓-│    Holes: [4,8], [5,11], [8,5], [8,15], [11,4], [11,14], [14,8], [15,11]\n" +
                        "1:13   ║....┌──╗....│    13   ║-B--┌──╗-B--│    LaserMachine: [4,14], [5,4], [14,15], [15,5]\n" +
                        "1:12   ║...Z│  ║Z...│    12   ║-☺--│  ║----│    Lasers: [5,5], [5,14], [7,5], [12,14], [14,5], [14,7], [14,14]\n" +
                        "1:11   ║.O┌─┘  └─╗.O│    11   ║--┌─┘  └─╗--│   \n" +
                        "1:10   ║.$│      ║..│    10   ║--│      ║--│   \n" +
                        "1: 9   ║..│      ║$.│     9   ║--│      ║--│   \n" +
                        "1: 8   ║O.╚═┐  ╔═╝O.│     8   ║--╚═┐  ╔═╝--│   \n" +
                        "1: 7   ║...Z│  ║Z...│     7   ║----│  ║--↓-│   \n" +
                        "1: 6   ║....╚══╝....│     6   ║-B--╚══╝-B--│   \n" +
                        "1: 5   ║....O$.....˂│     5   ║-↑-←------←-│   \n" +
                        "1: 4   ║.˄.....O...E│     4   ║------------│   \n" +
                        "1: 3   └────────────┘     3   └────────────┘   \n" +
                        "1: 2                      2                    \n" +
                        "1: 1                      1                    \n" +
                        "1: 0                      0                    \n" +
                        "1:  01234567890123456789   01234567890123456789\n" +
                        "1:Answer: ACT(1),DOWN\n" +
                        "------------------------------------------",
                String.join("\n", messages));

    }
}
