package com.prisonerprice.repository;

import com.prisonerprice.model.Album;
import com.prisonerprice.model.Artist;
import com.prisonerprice.model.Stock;
import org.junit.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;
import java.util.Set;

public class AlbumDaoTest {

    private static ArtistDaoImpl artistDao;
    private static AlbumDaoImpl albumDao;
    private static StockDaoImpl stockDao;
    private Stock newStock;
    private Album newAlbum, newAlbum2;
    private Artist Alvvays;
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Before
    public void init(){
        stockDao = new StockDaoImpl();
        albumDao = new AlbumDaoImpl();
        artistDao = new ArtistDaoImpl();
        Alvvays = new Artist(
                0,
                "Alvvays",
                2011,
                0,
                "xxxx"
        );
        newAlbum = new Album(
                0,
                "Antisocialities",
                2017,
                Alvvays,
                "Alternative",
                "xxxxx");
        newStock = new Stock(
                0,
                newAlbum,
                15,
                23,
                44,
                32,
                1
        );
        artistDao.save(Alvvays);
        albumDao.save(newAlbum);
        stockDao.save(newStock);
    }

    @After
    public void tearDown(){
        List<Stock> stocks = stockDao.getStockList();
        for(Stock stock : stocks){
            stockDao.delete(stock);
        }
        List<Album> albums = albumDao.getAlbumList();
        for(Album album : albums){
            albumDao.delete(album);
        }
        List<Artist> artists = artistDao.getArtistList();
        for(Artist artist : artists){
            artistDao.delete(artist);
        }
    }

    @Test
    public void getAlbumsTest() {
        List<Album> albums = albumDao.getAlbumList();
        for(Album album : albums){
            logger.info(album.toString());
        }
        int expectedNumOfDept = 1;
        Assert.assertEquals(expectedNumOfDept, albums.size());
    }

    @Test
    public void updateAlbumTest(){
        newAlbum2 = new Album(albumDao.getAlbumList().get(0));
        newAlbum2.setDescription("A Whole new Description");
        String originalDesc = newAlbum.getDescription();
        albumDao.update(newAlbum2);
        Assert.assertTrue(!originalDesc.equals(albumDao.getAlbumList().get(0).getDescription()));
    }

    @Test
    public void deleteAlbumTest(){
        List<Album> albums = albumDao.getAlbumList();
        for(Album album : albums){
            albumDao.delete(album);
        }
        albums = albumDao.getAlbumList();
        int expectedNumOfDept = 0;
        Assert.assertEquals(expectedNumOfDept, albums.size());
    }

    @Test
    public void deleteAlbumByNameTest(){
        Album deletedAlbum = albumDao.getAlbumList().get(0);
        int expectedNumberOfAlbums = albumDao.getAlbumList().size() - 1;
        String albumName = deletedAlbum.getName();
        albumDao.deleteAlbumByName(albumName);
        Assert.assertEquals(expectedNumberOfAlbums, albumDao.getAlbumList().size());
    }

    @Test
    public void getAlbumByNameTest(){
        String name = newAlbum.getName();
        Album testAlbum = albumDao.getAlbumByName(name);
        Assert.assertTrue(name.equals(testAlbum.getName()));
    }

    @Test
    public void getAlbumAndStockTest(){
        String name = newAlbum.getName();
        int expectedElementsNumbers = 2;
        List<Object[]> list = albumDao.getAlbumAndStock(name);
        for(int i = 0; i < list.size(); i++){
            for(int j = 0; j < list.get(i).length; j++){
                logger.debug(list.get(i)[j].toString());
            }
        }
        Assert.assertEquals(expectedElementsNumbers, list.size() * list.get(0).length);
    }
}
