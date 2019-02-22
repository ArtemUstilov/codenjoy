package com.codenjoy.dojo.web.rest;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 Codenjoy
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


import com.codenjoy.dojo.services.*;
import com.codenjoy.dojo.services.dao.ActionLogger;
import com.codenjoy.dojo.services.dao.Registration;
import com.codenjoy.dojo.services.nullobj.NullGameType;
import com.codenjoy.dojo.services.nullobj.NullPlayer;
import com.codenjoy.dojo.web.controller.Validator;
import com.codenjoy.dojo.web.rest.pojo.GameTypeInfo;
import com.codenjoy.dojo.web.rest.pojo.PPlayerWantsToPlay;
import com.codenjoy.dojo.web.rest.pojo.PlayerInfo;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletContext;
import java.util.*;

import static com.codenjoy.dojo.web.controller.Validator.CANT_BE_NULL;
import static com.codenjoy.dojo.web.controller.Validator.CAN_BE_NULL;

@Controller
@RequestMapping(value = "/rest")
public class RestBoardController {

    @Autowired private GameService gameService;
    @Autowired private RestRegistrationController registrationController;
    @Autowired private PlayerService playerService;
    @Autowired private Registration registration;
    @Autowired private ServletContext servletContext;
    @Autowired private Validator validator;
    @Autowired private PlayerGames playerGames;
    @Autowired private PlayerGamesView playerGamesView;
    @Autowired private TimerService timerService;
    @Autowired private SaveService saveService;
    @Autowired private ActionLogger actionLogger;

//    @RequestMapping(value = "/sprites", method = RequestMethod.GET)
//    @ResponseBody
    public Map<String, List<String>> getAllSprites() {
        return gameService.getSprites();
    }

//    @RequestMapping(value = "/sprites/{gameName}/exists", method = RequestMethod.GET)
//    @ResponseBody
    public boolean isGraphicOrTextGame(@PathVariable("gameName") String gameName) {
        return !getSpritesForGame(gameName).isEmpty();
    }

//    @RequestMapping(value = "/sprites/{gameName}", method = RequestMethod.GET)
//    @ResponseBody
    public List<String> getSpritesForGame(@PathVariable("gameName") String gameName) {
        if (StringUtils.isEmpty(gameName)) {
            return new ArrayList<>();
        }
        return gameService.getSprites().get(gameName);
    }

//    @RequestMapping(value = "/sprites/alphabet", method = RequestMethod.GET)
//    @ResponseBody
    public String getSpritesAlphabet() {
        return String.valueOf(GuiPlotColorDecoder.GUI.toCharArray());
    }

//    @RequestMapping(value = "/context", method = RequestMethod.GET)
//    @ResponseBody
    public String getContext() {
        String contextPath = servletContext.getContextPath();
        if (contextPath.charAt(contextPath.length() - 1) == '/') {
            contextPath += contextPath.substring(0, contextPath.length() - 1);
        }
        return contextPath;
    }

//    @RequestMapping(value = "/game/{gameName}/type", method = RequestMethod.GET)
//    @ResponseBody
    public GameTypeInfo getGameType(@PathVariable("gameName") String gameName) {
        if (StringUtils.isEmpty(gameName)) {
            return new GameTypeInfo(NullGameType.INSTANCE);
        }
        GameType game = gameService.getGame(gameName);

        return new GameTypeInfo(game);
    }

//    @RequestMapping(value = "/player/{player}/{code}/level/{level}", method = RequestMethod.GET)
//    @ResponseBody
    public synchronized boolean changeLevel(@PathVariable("player") String emailOrId,
                                @PathVariable("code") String code,
                                @PathVariable("level") int level)
    {
        String id = validator.checkPlayerCode(emailOrId, code);

        playerGames.changeLevel(id, level);

        return true;
    }

    // TODO test me и вообще где это надо?
//    @RequestMapping(value = "/player/all/groups", method = RequestMethod.GET)
//    @ResponseBody
    public Map<String, List<List<String>>> getPlayersGroups() {
        Map<String, List<List<String>>> result = new HashMap<>();
        List<Player> players = playerService.getAll();
        List<List<String>> groups = playerGamesView.getGroups();
        for (List<String> group : groups) {
            String playerId = group.get(0);
            Player player = players.stream()
                    .filter(p -> p.getId().equals(playerId))
                    .findFirst()
                    .orElse(NullPlayer.INSTANCE);

            String gameName = player.getGameName();
            if (!result.containsKey(gameName)) {
                result.put(gameName, new LinkedList<>());
            }
            result.get(gameName).add(group);
        }
        return result;
    }

//    @RequestMapping(value = "/player/all/scores", method = RequestMethod.GET)
//    @ResponseBody
    public Map<String, Object> getPlayersScores() {
        return playerGamesView.getScores();
    }

//    @RequestMapping(value = "/scores/clear/{adminPassword}", method = RequestMethod.GET)
//    @ResponseBody
    public boolean clearAllScores(@PathVariable("adminPassword") String adminPassword) {
        validator.checkIsAdmin(adminPassword);

        playerService.cleanAllScores();

        return true;
    }

//    @RequestMapping(value = "/game/enabled/{enabled}/{adminPassword}", method = RequestMethod.GET)
//    @ResponseBody
    public boolean startStopGame(@PathVariable("adminPassword") String adminPassword,
                                  @PathVariable("enabled") boolean enabled)
    {
        validator.checkIsAdmin(adminPassword);

        if (enabled) {
            timerService.resume();
        } else {
            timerService.pause();
        }

        return timerService.isPaused();
    }

    // TODO test me
//    @RequestMapping(value = "/player/{player}/{code}/reset", method = RequestMethod.GET)
//    @ResponseBody
    public synchronized boolean reset(@PathVariable("player") String emailOrId, @PathVariable("code") String code){
        String id = validator.checkPlayerCode(emailOrId, code);

        if (!playerService.contains(id)) {
            return false;
        }

        saveService.save(id);
        Player player = playerService.get(id);

        boolean loaded = saveService.load(id);
        if (!loaded) {
            if (playerService.contains(id)) {
                playerService.remove(id);
            }
            playerService.register(new PlayerSave(player));
        }

        return true;
    }

    // TODO test me
    @RequestMapping(value = "/player/{player}/{code}/wantsToPlay/{gameName}", method = RequestMethod.GET)
    @ResponseBody
    public synchronized PPlayerWantsToPlay playerWantsToPlay(
            @PathVariable("player") String emailOrId,
            @PathVariable("code") String code,
            @PathVariable("gameName") String gameName)
    {
        validator.checkPlayerId(emailOrId, CAN_BE_NULL);
        validator.checkCode(code, CAN_BE_NULL);
        validator.checkGameName(gameName, CANT_BE_NULL);

        String context = getContext();
        GameTypeInfo gameType = getGameType(gameName);
        boolean registered = registration.checkUser(emailOrId, code) != null;
        List<String> sprites = getSpritesForGame(gameName);
        String alphabet = getSpritesAlphabet();
        List<PlayerInfo> players = registrationController.getGamePlayers(gameName);

        return new PPlayerWantsToPlay(context, gameType,
                registered, sprites, alphabet, players);
    }

    // TOOD test me
    @RequestMapping(value = "/player/{player}/log/{time}", method = RequestMethod.GET)
    @ResponseBody
    public List<BoardLog> changeLevel(@PathVariable("player") String emailOrId,
                                            @PathVariable("time") Long time)
    {
        validator.checkPlayerId(emailOrId, CANT_BE_NULL);

        String id = registration.checkUser(emailOrId);

        if (time == null || time == 0) {
            time = actionLogger.getLastTime(id);
        }

        List<BoardLog> result = actionLogger.getBoardLogsFor(id, time, 100);

        if (result.isEmpty()) {
            return Arrays.asList();
        }

        // TODO Как-то тут сложно
        GuiPlotColorDecoder decoder = new GuiPlotColorDecoder(gameService.getGame(result.get(0).getGameType()).getPlots());

        result.forEach(log -> {
            String board = log.getBoard().replaceAll("\n", "");
            log.setBoard((String) decoder.encodeForBrowser(board));
        });

        return result;
    }
}
