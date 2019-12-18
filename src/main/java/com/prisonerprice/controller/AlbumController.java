package com.prisonerprice.controller;

import com.prisonerprice.model.Album;
import com.prisonerprice.model.Artist;
import com.prisonerprice.service.AlbumService;
import com.prisonerprice.service.ArtistService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping(value = {"/albums"})
public class AlbumController {
    private Logger logger;
    private ArtistService artistService;
    private AlbumService albumService;

    @Autowired
    public AlbumController(Logger logger, ArtistService artistService, AlbumService albumService){
        this.logger = logger;
        this.artistService = artistService;
        this.albumService = albumService;
    }

    @RequestMapping(value = "", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
    public List<Album> getAlbums(){
        List<Album> albums = albumService.getAlbumList();
        return albums;
    }

    @RequestMapping(value = "/with-children", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
    public List<Album> getAlbumsWithChildren(){
        List<Album> albums = albumService.getAlbumListWithChildren();
        return albums;
    }

    @RequestMapping(value = "/{albumName}", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
    public Album getAlbumByName(@PathVariable String albumName){
        Album album = albumService.getAlbumByName(albumName);
        return album;
    }

    @RequestMapping(value = "/{artistName}", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_VALUE})
    public String createAlbum(@RequestBody Album album, @PathVariable String artistName){
        String msg = "The album was created";
        boolean isSuccess = true;
        if(artistService.getArtistByName(artistName) != null){
            album.setArtist(artistService.getArtistByName(artistName));
            album.getStock().setAlbum(album);
            isSuccess = albumService.save(album);
        }
        else{
            Artist artist = new Artist(artistName);
            artistService.save(artist);
            album.setArtist(artistService.getArtistByName(artistName));
            album.getStock().setAlbum(album);
            isSuccess = albumService.save(album);
        }
        if (!isSuccess) msg = "The album was not created";
        return msg;
    }

    // have to provide the id
    @RequestMapping(value = "", method = RequestMethod.PUT, consumes = {MediaType.APPLICATION_JSON_VALUE})
    public String updateAlbum(@RequestBody Album album){
        String msg = "The album was updated";
        Artist artist = albumService.searchArtist(album.getName());
        album.setArtist(artist);
        boolean isSuccess = albumService.update(album);
        if(!isSuccess) msg = "The album was not updated";
        return msg;
    }

    @RequestMapping(value = "/{albumName}", method = RequestMethod.DELETE, consumes = {MediaType.APPLICATION_JSON_VALUE})
    public String deleteAlbum(@PathVariable String albumName){
        String msg = "The album was deleted";
        boolean isSuccess = albumService.deleteByName(albumName);
        if(!isSuccess) msg = "The album was not deleted";
        return msg;
    }
}