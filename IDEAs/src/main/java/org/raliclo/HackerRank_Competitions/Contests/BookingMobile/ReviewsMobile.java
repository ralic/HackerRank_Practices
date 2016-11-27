package org.raliclo.HackerRank_Competitions.Contests.BookingMobile;/**
 * Created by raliclo on 8/11/16.
 * Project Name : TestNG-1
 */

//https://www.hackerrank.com/contests/booking-com-passions-hacked-mobile/challenges/reviews-1

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.stream.Collectors;

public class ReviewsMobile {

    public static void main(String[] args) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        ArrayList<String> input = reader.lines().collect(Collectors.toCollection(ArrayList::new));

        int n = Integer.parseInt(input.get(0).split(" ")[0]);
        int m = Integer.parseInt(input.get(0).split(" ")[1]);

        ArrayList<String> ptypes = new ArrayList<>();
        ArrayList<Review> reviews = new ArrayList<>();
        HashMap<Integer, HashMap<String, Integer>> reviewers = new HashMap<>();
        HashMap<String, Integer> passions = new HashMap<>();
        for (int i = 1; i < n + 1; i++) {
            ptypes.add(input.get(i));
            passions.put(input.get(i), 0);
        }

        for (int i = n + 1; i < (n + 1 + 2 * m); ) {
            String[] data = input.get(i).trim().replaceAll("\\s+", " ").split(" ");
            int id = Integer.parseInt(data[0]);
            long timestamp = Long.parseLong(data[1]);
            String body = input.get(i + 1);
            reviewers.put(id, (HashMap) passions.clone());
            reviews.add(new Review(id, timestamp, body));
            i = i + 2;
        }
        for (int i = 0; i < reviews.size(); i++) {
            int id = reviews.get(i).id;
            int pts = reviews.get(i).pts;
            String body = reviews.get(i).body;
            HashMap<String, Integer> rvp = reviewers.get(id);  //Reviewer's passion
            passions.keySet().forEach(
                    (m1) -> {
                        if (body.toLowerCase().contains(m1.toLowerCase())) {
                            rvp.put(m1, rvp.get(m1) + pts);
                        }
                    }
            );
        }
        ArrayList<Reviewer> rvlist = new ArrayList<>();
        reviewers.keySet().forEach(
                (m3) -> {
                    rvlist.add(new Reviewer(m3, reviewers.get(m3)));
                }
        );

//        System.out.println(rvlist.get(0).id);
        ptypes.forEach(
                (m4) -> {
                    System.out.println(m4);
                    rvlist.sort((a, b) -> {
                        // Sort by Passions Type's Score // Higher first
                        if (a.passions.get(m4) > b.passions.get(m4)) {
                            return -1;
                        }
                        if (a.passions.get(m4) < b.passions.get(m4)) {
                            return 1;
                        }
                        // TODO : Be Careful of number object shall use "equals"
                        if (a.passions.get(m4).equals(b.passions.get(m4))) {
                            // Sort by User ID // Lower first
                            if (a.id > b.id) {
                                return 1;
                            }
                            if (a.id < b.id) {
                                return -1;
                            }
                            return 0;
                        }
                        return 0;
                    });
                    if (rvlist.get(0).passions.get(m4) == 0) {
                        System.out.println(-1);
                    } else {
                        System.out.println(rvlist.get(0).id);
                    }
                }
        );

//        System.out.println(passions);
//        System.out.println(reviewers);
    }

    public static class Review {
        int id;
        long t;
        String body;
        int pts;

        Review(int id, long timestamp, String body) {
            try {
                this.id = id;
                this.t = timestamp;
                this.body = body;
                this.pts = this.countPt();
            } catch (Exception ex) {
                System.out.println(ex);
            }
        }

        int countPt() throws ParseException {
            int pts = 0;

            try {
                if (body.length() >= 100) {
                    pts += 20;
                } else {
                    pts += 10;
                }
                SimpleDateFormat oldfmt = new SimpleDateFormat("MM dd yyyy");
                Date date1 = oldfmt.parse("06 15 2016");
                Date date2 = oldfmt.parse("07 15 2016");
                // System.out.println(t+ " " + date1.getTime() +" " + date2.getTime() );
                if (t >= date1.getTime() / 1000 && t <= date2.getTime() / 1000) {
                    pts += 20;
                } else {
                    pts += 10;
                }
            } catch (Exception ex) {
                System.out.println(ex);
            }

            return pts;
        }

    }

    public static class Reviewer {
        int id;
        HashMap<String, Integer> passions = new HashMap<>();

        Reviewer(int id, HashMap<String, Integer> passions) {
            this.id = id;
            this.passions = passions;
        }
    }

}

/* input
97 774
the
and
to
a
is
you
of
cairo
in
i
for
are
it
city
people
pyramids
very
was
with
but
not
at
be
visit
museum
as
there
if
have
great
so
they
we
good
that
on
place
get
see
all
food
places
time
from
go
or
will
hotel
nice
nile
traffic
egyptian
around
can
your
were
do
my
amazing
friendly
like
more
much
egypt
would
an
just
by
many
this
money
no
history
taxi
one
must
trip
also
lot
giza
take
has
don&#39;t
which
me
guide
old
it&#39;s
than
stay
some
try
most
really
experience
its
well
48 1468393528
it is definately a culture shock but once you get over it it is fine  stay away from big hotels like the hilton ramsesyou will get a negative impression of cairo  once we moved to egyptian night hostel we had an amazing time  big hotels also draw attention to people as tourists  the best word to know is &quot;le shukran&quot; which means no thank you
96 1468487604
go to the white dessert! really cool toor and won&#39;t take you more than 2 days &#47; 1 night (shortest one)
56 1468497070
sites were amazing  it was very safe -- far safer than we expected and probably safer than a typical american or israeli city  but not much english was spoken and we were glad we had hired a guide to show us around  compared to the us the driving in the streets appeared like pandemonium there were almost no traffic lights lanes even when they existed were simply ignored; and cars routinely drove within a foot or so of each other (but we saw no accidents so i guess those egyptian drivers knew what they were doing but it did not feel that way)
46 1468497540
the best place for me
24 1468498428
cairo is a great city very crowded with so much pollution and noise but still in my opinion a nice place to visit specially to visit the amazing highlights an monuments  i love the night life in cairo  the people are so friendly
44 1461222728
tahmir square cairo tower caito museum coptic (old) town downtown and of course the great pyramids of giza do much to see great place to immerse yourself so busy and alive amazing beautiful stunning history &amp; cultural buff&#39;s dream
20 1461223722
nova hotel i stayed there for most of my time
21 1461227018
an increibily bustling city full of amazingly friendly people and a history thats mind blowing
27 1461258495
cairo is a chaotic city filled with trash and too much traffic and noise and people go for the top hits for tourists - sphinx&#47;pyramids egyptian museumother sights that might interest you (old islamic and old coptic quarters for ex and some interesting cultural performances such as sufi dance) - then get out
87 1461337702
first of all the place is safe at any time beautiful place with friendly people too much history of the humanity!! try to learn some arabic words numbers for easily communicate with no the traditional touristic related people thats obviously talk a lot language!    must go&#47;do:  -piramid  -cairo musium  -eat coshary  - visit&#47;buy market close to islamic cairo area
53 1461406077
haven&#39;t seen much just an italian restaurant a lot of traffic around 6-7pm
15 1461410326
the people were extremely helpful and friendly   the security is higher than anywhere we have seen   don&#39;t drive - take taxis they are very reasonable and you can then &#39;enjoy&#39; the ride it&#39;s an education not to miss   go to the egyptian museum - a must see place   food is good   water has a reputation for not being the best so safest to ask for &#39;no ice&#39; when getting drinks   never seen so many hotel staff and they couldn&#39;t have been more helpful   tipping is standard so try and have 10 &amp; 20 pound notes - in english money that&#39;s only a £1 or 2   best to go on organised tours - will cost you more but saves time and uncertainty   in conclusion - would love to go back
32 1461456035
very polluted and dirty city
29 1461490395
rich culture and architecture must visit once in a lifetime especially for the pyramids
97 1461491211
piramides
35 1461492969
friendly environmentfriendly taxi driver from pyramid resortname adeleoverall welcoming peopleamazing
46 1461498166
only go for the day if you want to see the pyramid and other monuments toooooo much traffic in cairo shopping is very cheap in cairo we stayed in hurghada for 8 days and it was much more peaceful and relaxing
96 1461500095
to find some where to eat it&#39;s very hard and to find the right food not easy
88 1461505267
stick to the old culture  amazing stories and history
98 1461506026
my hotel as a whole was good except wi fi service was very poor breakfast was of very ordinary type but cairo is a great historic city one must go and visit
28 1461511569
lovely city very busy and hectic at time but fantastic place
91 1459251960
i loved the pyramids and the guide explaining the history
19 1459254531
pretty much everybody tries to rip you off
70 1459262744
pyramids were amazing if you&#39;re going get yourself a guide before travelling to pyramids   also tutankhamen exhibition is also amazing     eat at sequoia great food and great views    negotiate with taxi drivers - usually half to a third of what they initially ask don&#39;t get in without agreeing price  driving is like bumper cars at speed terrifying at times    nile cruise is very average; there is little to see scenery wise on the cruise food on board is average     if you are asked by a local to visit their gallery - don&#39;t they only want to sell you something
40 1459268038
cairo is noisy dirty and the driving is a nightmare however a short drive to saqqara and you are back in time over 4500 years at the funerary complex of djoser the imhotep museum the serapeum the pyramid of teti and the tomb of mereruka   the cairo museum is a must and of course the great pyramids of khufu snefru and khaefre and the sphinx taxis are white with a broken blue stripe down the side always agree the price first a trip to the museum from giza should cost around 60ep never use a private car they are not insured for tourists and will overcharge
99 1459274068
بلد عبارة عن حديقة زبالة الزبالة في كل مكان تشعر انك عايش في حاوية زبل
71 1459279933
very busy noisy and everyone is trying to steal from you in any way
92 1459330689
-  castel mohammad ali   - al- moez road   - al-   abd   ice cream shop   -cotonile  purchasing   - peramids   ofcourse   -i did not like the streets to piramids  broken  under construction flooded must do well  -  al- qanater    - blue nile ship &#47; restaurant   - the nile rver trip   - the farrows  village   - historical sites   all over ( al husain and so )   - did not like the poorness croud around the  hussien shrine should be controlled   not to drive tourist away from such locations
12 1459346556
for staying in cairo must be choose a hotel of three stars and above &amp; high viewer recomended  every things else ok
17 1459386628
islamic cairo and cairo citadel was amazing!
87 1459397748
such a historic city lots to see but its a little &quot;in your face&quot; cairo is crowded has traffic issues and is generally not the cleanest place - but this is where you get to see thousands of years of history mosques and churches authentic middle eastern bazaars and some wonderful food should definitely be visited     be careful of touts and note that the people here are often looking to make some extra money for a living so you may be harassed at some places but its all part of the experience
15 1459407813
cairo is great for tourists interested in monuments as well as in night life it is a city full of vitality i didn&#39;t like shopping in cairo the sellers always try to deceive you all in all it was a great experience to go there
63 1459408482
all places in cairo are beautiful it is really a nice city
7 1459416948
downtown cairo is great! you can find pretty much anything make mental notes of landmarks or a particular store and you wont get confused with directions people are willing to help you find your way and will go as far as walking with you to ensure to find the place you seek   gad - excellent variety and great food!
58 1464550692
the opra house
21 1464612306
big city of africa and very impressive and easily to travel the historical of egyptian style
57 1464613929
cairo has a rich history and treasures of past civilizations in easy access of the city  for the solo traveller informative guided tours are recommended to get you to what you choose to see and miss queues local cuisine was good      cairo traffic is crazy with queues and noise over often bumpy roads  what is an interesting local experience to start with quickly becomes a time-wasting annoyance    sadly some parts of the city are covered with rubbish abandoned building rubble and litter on waste ground and river banks revealing a lack of civic pride and uncaring throwaway culture
17 1464615249
fabulous place
16 1464615867
amazing experience and people were very helpful language is an issue so it helps having a trustworthy tourguide unfortunately tourists are targeted with high prices for items which would normally sell for much lower prepare to negotaite a lot uber is very convenient to travel between tourist destinations when there is wifi or if you have international roaming it is also a lot cheaper than the transport arranged by the hotels or traveling agencies we made good contacts which really assisted with unexpected arrangements
56 1464620236
القاهرة حلوة كتييير قعدنا فيها 8 ايام وما كانوا كفاية الاماكن اللي فيها ما يتخلص   من احلى الاماكن اللي زرتها في حياتي وان شاء الله ناوية اكررها
39 1464691306
the pyramids and the sphinx   the museum   the nyle river  the night show of the pyramids
62 1464696286
i had the best guide! i loved the people of cairo  it was fantastic for my entire family  https:&#47;&#47;wwwtripadvisorcom&#47;attraction_review-g294201-d2386364-reviews-emad_abu_hadeda_private_day_tours-cairo_cairo_governoratehtml
54 1467105652
friendly people a lot of attractions to see
28 1467119742
cairo is a bustling amazing city of great people and with an amazing history thoroughly recommended
48 1467157522
guided driven tour of pyramids and key sites on our first active day give great perspective but it is recommended to be circumspect about spending a lot before you have greater experience of value the licensed guides will take you to government registered outlets which is good as you know you are buying genuine quality with things like papyrus or unique essential oils perfumes the bad is that you may regret spending so much later when you have more experience don&#39;t be afraid to ask for the shops card praise the merchandise quality (some is fabulous) and promise to come back which is easy to do with uber   the best fun was taking the cairo metro subway across town to see tutenkamen at museum next to tahrir square  sarah subway station 1egp = 10cents us each ticket!   download a subway map for your smartphone from cairo metro web site
90 1462542511
cairo is a messy noisy dirty town with beautiful people beautiful old buildings and fabulous monuments i can  be pretty daunting at first but when you get used to it it has many hidden gems    make sure that you get in touch with trustworthy locals beforehand (who have no financial interest in you) and ask for best tips: how to get around how much should i pay for this or that etc some strangers however will get out of their way to make you enjoy your stay take time to get to know the people
5 1462647620
make use of the metro to get round visit sakara and dashur  the sarapeum is mind blowing
16 1462648508
visit abusir sakara and dashur try to get a good guide if you stay in sakara use your host
66 1462688937
thank you cairo for the great history!!! (&quot;)
57 1462700504
the museum the pyramids the markets are great  pushy salesmen selling questionable goods and &quot;guides&quot; offering services were a pain in the rear
29 1462700565
use uber taxi&#39;s
6 1462701667
very friendly locals
20 1462712073
pyramids visit
98 1462713227
lots to do the opera house has some good eventsconcerts and opera so check with the concierge   the cairo museum is a must visit   take a trip down the nile on one of the big boats great food and entertainment   some great shops just off tarq square   use uber taxis much cheaper and very reliable
3 1462713514
cairo is a must visit place to see the pyramids and the egypt museum however for everything else it&#39;s noisy dirty polluted and riddled with traffic and hawkers preying for tourists not my favourite city but a great addition for an adventurous traveller&#39;s list
59 1462719407
lovely  warm welcoming country
29 1462730187
everything
66 1462742045
amazing place!!!     visit the pyramids and khan el kalilli market the people are great traffic is horrific but it is part of the experience!
28 1462751741
seeing the pyrimdsalthough the egyptians could better and provide more protection to  tourist  food is awful in cairo and you always have a price for egyptians and one price for tourist  most money spent was on taxis  everyone wants a little extra on top of their service which is pretty annoyting  cairo is filthy i used to return home and had to scrub just get the dirt off me
2 1462798135
wonderful city love it
51 1462827014
nice place to visit full of culture and sights the place seemed very secure despite the bad publicity it has    you have to be careful as almost everybody that sees you&#39;re a tourist will try to cheat you in order to get your money   the way they drive in the city is just crazy
35 1462835301
cairo is heavy and rich in its cultural places and heritage  you could spend more than a week without visiting all the sites you want  the only drawback that there is no continuous maintenance and cleaning for such awesome places
98 1460137322
walk beside tge nile river i ate @ abou shakre paul abou al seed  swiss restaurant am pm restaurant paul and sufi cafés it was really easy to get around cairo as my friend recommende for me a tour guide with his car he took me to very nice places and he was kind kowledgeable and hard working person his name tamer hamdi and his number is +201117044603 just in case any one want to contact him i really enjoyed the trip on the nile pyramids opera house museums al azhar al orman park&#39;s anot toca boca gift shop at hassan aflaton st etc go to the east go to the west egypt is the best god bless eygpt ☺
93 1460137864
walk beside tge nile river i ate @ abou shakre paul abou al seed  swiss restaurant am pm restaurant paul and sufi cafés it was really easy to get around cairo as my friend recommende for me a tour guide with his car he took me to very nice places and he was kind kowledgeable and hard working person his name tamer hamdi and his number is +201117044603 just in case any one want to contact him i really enjoyed the trip on the nile pyramids opera house museums al azhar and al orman park&#39;s etc go to the east go to the west egypt is the best god bless eygpt ☺
61 1460195790
as one of most busy city where you can find what you need  civilization  museum  pyramids  khan el khalili    good food  very reasonable prices for sightseeing  opera house  fresh fish at alexandria and ain soukhna &#47; red sea
63 1460197156
its a nice place must visit museumand nile cruise along with the pyramids  do try the traditional food
26 1460232588
tour guide was excellentvery knowledgeable  airport security was excellent  friendly locals great places to visit
63 1460238607
crazy traffic  every thing to finish it take&#39;s longer it should be
72 1460273040
24 hours live city with everything you could imagine
26 1460280212
:)
39 1460281032
even the police begged for money
43 1460282084
we have visited interesting places delicious food but the only negative point is heavy traffic during some hours
64 1460284029
nice
92 1460295325
the weather was nice and the place
29 1460306391
big busy city traffic is an issue start your day trips early (the pyramids open at 6) to ensure less time in traffic and to beat the crowds - which due to the political climate are not many you can pack a lot in in a couple of days
93 1460309335
pyramids khan kalilimuseumnile
18 1460313772
lovelyyyyyyyyyyyyy
64 1460316624
i stay one day at cairo most of the time i was out cairo
66 1460332891
good city
6 1460350638
i liked the people their soo kind
82 1460362579
wonderful city with too many to see
13 1460368477
cairo is a lovely lively city
32 1460385225
well cairo is really huge and as all big cities around the world you can easily realize what huge cities means regarding city center with a bit complicated roads which can be easy following the signs that if yourself driving easily you can see somewhere the cleanliness not like you wish but it&#39;s normal  for the recent period and about the trouble we hear in the media about the general situations in egypt can say it&#39;s totally safe and don&#39;t believe what media says
49 1460443707
good enghe
22 1463477487
i like the egyptian traditional food
89 1463482566
nice people and historical heritage
13 1463484145
nice but too crowded egyptians are very welcoming people
86 1463486237
cairo and egypt is a must to see every city and village in cairo has a history if cairo amongst many other thinks there is the pyramids and the egyptian museum  luxor there is the valley of the king and the valley of the queen and what about the nile cruise!!! i can write a book about all this what i can say it is a must to visit in your life time
86 1463510819
amazing capital city
13 1463550269
i went with an impression that cairo is not so tourist friendly but after meeting my guide and driver i changed my view giza area is pretty dirty and considering that that&#39;s the show case area for the pyramids a lot needs to be done to get that right  cairo by night on the the nile cruise is awesome and a must do thing  the museum just can&#39;t be missed for all the history the pyramids of course is what we went for and not disappointed
93 1463550966
it is a europe in middle east and africa
89 1463570782
egypt is beautiful with the river nile  is fantasticit it is quit enough for old lady like me to just stay in this hotel&#39; balcony  and enjoy the veiw for the whole stay and not to think about  anything else to do  just relax and enjoy it is realy a perfect hotel with perfct veiw
14 1463605015
its the best for a vacation
67 1463636086
it is better to take a hotel some where in the city centre if you want to visit cairo
16 1463652003
i know cairo and like it
80 1463653603
the weather was too hot not good sunny hot but icky hot  everyone trying to hustle you for money bad hygiene dirty streets filled with garbage many beggars  be aware so you don&#39;t get food poisoned
8 1463655035
pyramids  city stars mall
97 1465074644
museum and pyramids excellent we went gizza siqqara and memphis and dashur all interesting nile cruise dinner was amazing khan e khalily bazar was full of fraud coptic cairo not interesting islamic cairo little interesting cairo tower is very costly ticket
73 1465117570
cairo is so beautiful a place were there is history of thousands years ago of the great poharaohs cairo means you are going to feel oriental taste at khan el khalily area and thanks to a huge number of places to visit and an endless shopping gate and the egyptian&#39;s people hospitality  is amazing :)  i recommand you to come and visit egypt cairo as well as luxor and assouan relaxing natureand charm el cheikh enjoying the big red sea where you can do diving snorkling paraseiling  visit egypt you won&#39;t regret it ;)
59 1465121651
arrange for your travel mode within egypt well in advance trust only registered tour operators through your hotels or online be very open to negociations when purchasing any good or services in cairo
1 1465131075
lots of things to see in cairo and nearby the egyptian museum is a big must also the mosque ibn tulun and the bazaar khan al-khalili   we recommend el abd patisserie very good icecream  cairo is very easy to get around cheap and fast metro system and taxis
7 1465145185
loved city star mallcafe supreme excellent service loved the shisha and wafflesbooked viator for 2 days and it was brilliantvisted khan el khalil bazaar which was really nice egyptian museum is a place to visit especially to see the mummiescairo is a place to visit for shopping and sightseeing lovely and helpful people
12 1465156036
do not visit this shit hole!!! it is dirty and congested i visited the pyramids and at every turn they were asking for tips and money do not visit this shit hole on my way back i was extremely unwell due to food poisoning requiring emergencies on the plane  i would not recommend this place to anybody so please do not go there
76 1465201316
easy to go around congested traffic not easy to cross the road nobody respects traffic rules
38 1465221594
good place
64 1465224709
the piramides nd the nil
53 1449171485
a historical place a lot to visit  better with some one local knows the way and dealing with people  better if you negotiate with local driver(taxi) waiting for you  around 300 egyptian ponds a day  a lot of sense of humor around you    don&#39;t drive in cairo  unfortunately its very dirty city feel sorry for such a historical city that local people don&#39;t appreciate it
71 1449242657
i like the market area very much! colorful busy interesting!  al khalil souk!
23 1449244277
excellent tourist  attractions  history  and  culture  warm friendly people  keen to  assist
50 1449328539
you could have a lot of things such as:  have a dinner on the river nile   the giza pyramids   visit the egyptian museum   check out any concerts or programs going on at the al sawy culture wheel   visit al azhar park is one of the famous and most popular parks in cairo you might get sick of cairo and the heat and traffic so you should take 2 - 3 days off and go to sokhna or sharm el sheikh   you could go spend a few days in alexandria go to the library visit montazah park
51 1449333416
the light and sound show at the pyramids was unique and a must watch at least once the nile river boat ride was also very enjoyable but beware of cons in the name of &#39;bakshish&#39;
53 1449339629
cairo is a great city friendly people traffic can get very busy so plan your trip ahead allow for extra time
72 1449340843
city star  fish market  no  please try to add vip meet and asst  service throu your bookingcom as thy charge 100 dollars
40 1449399206
pyramids  museum  tahrir square  fish market  sequya  khan el khalily
40 1449415179
i like everything  resturant is everywhere  prices is cheap and this guest house is suitable for students
19 1449516674
i liked all the things you might expect the museum pyramids etc the best place for me was the citadel and mohamed ali mosque from there the views were spectacular however what really spoils the trip and i have travelled extensively is the hassle i know the tourist trade is suffering in eygpt but you are hastled all the time everybody it seems wants your money for even giving you directions my advise to other travelers would be  be very careful and don&#39;t book any trips until you know the cost of things and as sad as it sounds if someone approaches you in the stree it&#39;s because they want something from you also watch for tour guides taking you to shops etc they get commission and can be persuasive
17 1449580573
you simply feel like home
88 1449652445
i lived in cairo for 15 yrs and headed back to see friends cairenes are so excited to have foreigners visiting kids ask to have photos taken with you just because you are from away tourism is suffering badly and the touts have to ask higher prices if they are only get to get one sale it has to be a good one but step away from the main tourist areas to find the real cairo - friendly funny and sometimes crazy! on the surface cairo is a filthy third world city the gems are hidden behind non-descript doorways and down random alleys and in the people of the country they can take a bit of work or a befriended egyptian to find
33 1449829379
for me cairo is my second home i have a full knowledge about people and places i love this country forever
29 1449844530
the overall charges were rather expensive than before
74 1449853637
cairo has changed we went to the pyramids and there very very few if any tourist police very few tourists are there at the moment and if you go on an unaccompanied tour you get harassed by all the vendors people were climbing the pyramids and the experience was very different to the one about 9 years previous egyptian museum now allows cameras but it was sad to see how it has been damaged over the years
46 1449906453
cairo is big enough to accommodate all words  it is not a city it is a journey in history and today towards tomorrow  looking forward to smell the city spirit and the culture then go to cairo and enjoy the stones  sun moon sands nile  pyramids and many more but on the top of that a kind  smile  every where
0 1450009739
i love cairo for many reason i could advice everyone should visit cairo  so many place to go for both reason especialy religious and tourism its a amazing place    weather was very nice shopping relaxation sightseeing and many more cannot express how much i enjoyed really very very nice place
29 1450013000
n&#47;a
30 1450018114
as long as you stay in hotel in good location  using uber and not a taxi  avoid rush hours  tourism destinations   you would have fun
17 1450058821
they try to cheat foreigners wherever they can try to figure out online how much you are supposed to pay for the different things before you go
35 1450073541
nice weather   nice location for visit   fantastic city
71 1450135799
i like every thing  simpl lovely people also cheap prices   safe for any lady if she is  along  good picnic resturants areas of tourism  everything
95 1450216537
the positives far outweigh the negatives positives being the pyramids and sphinx and obviously the amazing egyptian history; cairo museum (this museum a bit &quot;tired&quot; now but a new one being built at present); the citadel and mosque; old cairo  be prepared for a city of approx 20 million people unfortunately cairo has a huge garbage issue so also be prepared to see mounds of rubbish in certain areas brush up on tipping skills as tips are expected and it&#39;s handy to have amounts in mind for various services  concentrate on the positives and you will not be disappointed
84 1450255032
pyramids of gizah national museum
7 1450261519
les gens sont sympas beaucoup d&#39;activités apprécié la météo et la qualité de vie musique théâtres et musées  shopping etc
37 1450280780
traffic -
65 1450321655
cairo is a capital with exceptional sights for tourists wonderful shopping interesting restaurants and warm friendly people in particular i would commend the kempinski hotel its outstanding staff and the exceptional tour guides (in particular andrew) who they provided overall my experience of cairo will remain memorable
37 1450368157
the nile is magnificent
12 1450415440
i must say that all the egyptian people were very friendly and more than happy to help however some people took this help to the extreme and expected a tip for their services or information but there are those who genuinely have a heart to look after the visitors to their country and will make sure you are safe and provided for our highlights in cairo were the giza pyramids staying at the pyramids loft across the road and the egyptian museum
75 1450516523
people so friendly shopping for cloths historical places sightseeings food
43 1450557810
recomiendo sólo un día  por la mañana las pirámides y por la tarde mezquita y museo
4 1450625046
cairo is always the greatest city to have vacation in
70 1450669016
since i am egyptian so i am familiar on how to get around in cairo egypt but others will need tourist guides or travel agencies to help in visiting the various places in cairo egypt also i enjoyed walking in the streets than being in a taxi during sometimes non-moving traffic
59 1450705945
nice city traffic and smog is horrible history is excellent
62 1450732784
a must see is of course the pyramids     i also visited some mosques - worth a note that the blessed footprint of our prophet (peace and blessing be upon him) is in the imam shafi mosque
28 1450804080
1 cairo is a great city with a lot of things to do however plan your trip well as the traffic is bad  2 be aware (and prepared) to pay some money her and there (gifts to say the least) as people can do things for you and expect a return this has become less since my last trip to cairo 7 years ago  3 it&#39;s disappointing how dirty the city has become since i last visited cairo years back even the famous attraction sites (eg: pyramids) are full of dirt around its streets in the pyramids you see the animals&#39; drippings everywhere so be careful  4 i would still come to cairo anytime the people are honest and nice and it&#39;s truly worth it despite the short comings
79 1450804413
the different sites are great still have a lot to see there but that will be next timefood is very nice too the pollution rubbish everywhere and lots of people trying to scam you all the time making up stories lying being pushy and who doesn&#39;t take no for an answer is very annoying we didn&#39;t really buy any souvenirs to bring home or anything at all because it&#39;s too much trouble trying to find the real thing and the real price if people were more sincere and honest i&#39;m sure we would have liked to look around in shops and markets but the way it is now we were just trying to avoid these people and places
78 1450806153
from landing at airportpeople are very friendly and helpfulfavourite places: pyramidssphinxmuseumadvise to do these tours with a tour guidefood at hotel is expensive but you can go to eat out at nearest mall to the hoteltaxis outside the hotel are reasonablethose in the hotel are very expensivetours with very small groups &#47; private tour is the best( they take good care of you)
20 1459430681
cairo is not the nicest place to visit to begin with you have to be a good bargainer to like it whenever you want to buy something you have to go through the hassle of bargaining less you are willing to throw off some unwanted money beware egyptians like ripping foreigners off i am muslim and could speak little arabic but don&#39;t be fooled that wont save you from being exploited in egypt if you are non muslim and particularly white you are likely to be more vulnerable to exploitations one time we took a taxi to a nearby place and on our return the driver took us through far away routes  in order to increase meter reading the immigration guys are also pretty bad they ask you unnecessary questions i am a dual national and the immigration officer asked me what my original nationality was to which i replied it is non of your business     finally i would rate egypt 1&#47;10 if you are planning to take a holiday take your money where you would be respected and provided with a decent service
68 1459432726
i loved the trip to the pyramids khan al khalili al azhar gardens yet the traffic was bad all the time  we had good food at  kababji in sofitel al gezirah and at the platform in ma3adi
92 1459437180
amazing
64 1459450581
the history amazing  get on a camel and visit this landmark
26 1459468053
never sleep
89 1459507217
cairo is for explorers
96 1459519152
i enjoyed the national food at abu-sid restaurant at city stars mall and lebanese food at beit ward at porto cairo and shisha too was so nice shopping was nice in down town mall cairo festival mall &amp; city stars of course the nile cruise dinner with the show was lovely and the food too the egyptian history stunning at the national museum cairo had really a night life which it starts from 12 midnight till morning street #9 in maadi is nice too your visit to cairo can&#39;t be once for sure you would love to come again
61 1459555994
vulture city be prepared to constantly be fighting off people wanting to sell you something and it ain&#39;t cheap either they don&#39;t just want a few dollars they want as much as you can cough up!
67 1459583624
evrything is good
24 1459590647
superb
84 1459590861
closer to the nile closer to the life and eating at burj al qahira restaurant at the top of the city is something amazing
98 1459594704
hire experienced check in front desk staff
69 1459594844
great city   great history  very nice people
8 1459600623
from a business traveller point of view:  - cairo ia a city you have to go even if you don&#39;t want (i didn&#39;t want) because its the capital of egypt and every main&#47; head office of a company governmental organization or educational institute is there  - i didn&#39;t like that cairo is too crowded and always there is traffic so it is not easy to move from place to place  - food is good and not expensive and always you have varaities
22 1459600778
the visit to the pyramids of giza
50 1459601663
if you want to see the pyramids then book a hotel close to the pyramids allow a day or two visit the pyramids and any other locations - no need to hang around cairo traffic is dire infrastructure looks unstable getting about is a nightmare    zahra park was a great retreat far from the pyramids but once there it was somewhat relaxing
63 1459609107
cairo&#47;giza is a crazy unbelievably noisy city but there is a lot to see and do
63 1459676705
it&#39;s really good
2 1459677157
cairo is an intense experience most people are friendly considerate and helpful   going on horseback through the desert to the pyramids was the highlight for me in cairo the metro and trains were easy to use i booked the train from cairo to alexandria first class two weeks before the time and it was a pleasant way to see the country   the aggressive marketing is annoying though and yes you as a traveler&#47;tourist get overcharged at every turn and corner a cup of coffee will cost 6 le on the train but the same coffee will be 20 le at the comfort stop on your way to luxor and they call it &quot;doing business&quot; maybe tourism will pick up if they can shake off this bad reputation of exploiting the tourists
4 1459679163
great city full of love culture history and climate change one of the most beautiful cities in world
25 1459679648
great ancient city! cairo is indeed an eternal city as long as the heavens and the earth has not passed away
99 1459681154
1the pyramids citadel khan el  khalili  2 le pacha sequoia cairo capital cakes zooba marriot hotel left bank  3  kind of too much traffic
22 1459681239
people were so nice very supportive
28 1459682153
struggling to find the words very frustrating place to be take a selfie stick if help is offered they&#39;ll want paying for it even if a stranger offers to take a photo for you if you don&#39;t pay you will not get your camera back
92 1459688919
i visited luxor and stay there for for 4 days then i visited cairo for 5 days it was a nice trip we enjoyed
13 1459696903
i love this city despite its dirtiness and noise
17 1459697794
pyramids nile cruises aqua park dream park salah al din castle cairo tower nile coffeespharaonic coptic and islamic museums
31 1459752107
aside from giza cairo still boasts several beautiful attractions for the traveller and culture vulture the museum is probably the best attraction but there are several lovely old mosques markets and soukhs to see that make it a worthwhile trip cleanliness and congested traffic are a major issue but cairo is also very well located not just for internal flights and train rides to luxor aswan alex and red sea resorts but for many international destinations within africa
58 1468163990
wonderful place with all the info on history found in luxor
34 1468169677
very dirty city too hard to get information poor internet services
21 1468180926
amazing city  but the tourist sector is definitely run by very unprofessional people
20 1468217592
fun
61 1468236158
charming and kind people
6 1468278812
alex
22 1467391386
einstein kaffee city stars
38 1467419501
a very interesting city so many attractions to visit easy transporting by taxi (but try to negotiate on the price before you ride you can go down up to 35% ! never use a meter-counter as they might elongate the way to get more money) metro ride is a good and very cheap alternative (but some lines are old with no air conditioning in the hot summer) busses are also available cheap food and hotel prices compared to ours in northern europe mostly i admired the egyptians for being polite co-operative sociable and lovely people they left an excellent impression on me the pyramids new malls down town shopping centre many mosques and other  several museums (allow good time for the egyptian museum) and much more are worthy to be visited
70 1467443828
六月底去埃及是极大的错误，天气太热……it&#39;s too hot to go egypt in june  宰客、拉客、要小费情况比较多。 tourist defrauding is common and if you go to the pyramids without guide the local people will keep disturbing you to receive their service even though you refuse it&#39;s a kind of annoying and everyone there wants tips from you even the formal staff    埃及物价低，但也只限于去那些明码标价的大超市或者餐厅才能感受到。commodity price is low but you should you to places where the prices are marked clearly like big supermarket and chain restaurants  当地人对外国游客很热情，乐于帮助，但是需要区分哪些是热情帮助哪些是想拉客赚钱。  the local people is nice and helpful
43 1467462447
see the museum and the pyramids then get out
51 1467487421
it is a strange place its quite run down and dirty in places and the traffic is just crazy but that has not turned me off i was quite surprized just how much i loved it
1 1467499618
cairo was a great place  people  so friendly  could not have felt  safer  in egypt
40 1452838740
u need to get use to the cars&#39; horns)) and be careful crossing the street just follow the local people)) enjoy egyptian food but dont forget it is spicy)) it&#39;s better to ask for a taxi from the hotel and deal the price before go i wouldn&#39;t rent a car for driving in cairo coz i think only local people can manage such crazy driving))) the price in menu will be different in bill coz tax and service will be included in bill
2 1452857306
i love cairo but 4 days is always enough then  i  am ready to leave the madness of it i like to shop and there is lots of that when you are living in luxor where there is very little plenty of things to do to fill your time and would recommend it to all
25 1452896051
u will fined nice lovly people  safe city  cheap prices good places for picnic in gardens  nile river or museum and pyramids and many many things
33 1452907656
-try to stay near metro station to visit pyramids use metro stations and do not hire a car &#47; guide as they will charge a lot  -the taxi fares from giza station to pyramids must be around 20 egyptian pound  do not pay more than that  -be very careful while purchasing tickets check the amount properly before handing over the notes plzz !!  -khan al khallili bazar is worth visiting but for shopping plzzzz buy the stuff from luxor souq   -i recommend every1 to visit luxor before visiting cairo to better understand the ancient history!!!
66 1452941999
i by far enjoyed the pyramids
68 1452977395
history and nice helpful people    very crowded city good at night great food and reasonable price    i did visit the city of luxor it was the highlight of my trip    clean comfortable hotels [winter palace]all the ancient history is     fascinating easy to get around by taxi it is cheap chose london taxi
37 1453030697
i loved the museum tour you practically need to read ahead before visiting since a lot of relics and very ancient exhibits lay there the opera house and arabic music and heritage is not to be missed cairo tower is a bit old fashion but the rotating restaurant is something that i am welling to try next time
52 1453040788
hotel
51 1453043913
pyramids are great but the hassle and hussle suck
80 1453048547
don&#39;t miss the mummy room or the nileometer see if you can find seneca crane in the egyptian museum
0 1453049412
first day we visited the hanging church the jewish temple and amr ibn elass mosque then we spent the rest of the day with a guide at the egyptian museum which is amazing  the second day we saw el moez street (old cairo) and khan el khaleili and had dinner at naguib mahfouz cofee&#47;restaurant and listened to the live oriental music there wish we had more time to go to more places
1 1453135757
rude people everywhere
58 1453168785
cairo is really nice city that you can see everything is once  but the most important part that i want you to know about is the people are really friendly  + the police is everywhere
89 1453189933
there is a great disparity in the places nightlife prices and the amounts are very fancyyou feel that you are exposed to theft wherever you go and this feeling makes you tire of accommodation
6 1453205570
nice restaurants in the shopping malls specially lebanese restaurants   it is very difficult to move in certain areas in rush hours it took us more than 3 hours to reach to pyramids side from our hotel in the evening
54 1453215884
the secret of cairo is going to the downtown and see the old markets how are things is going on
28 1453216809
cairo is good place if you like the tradtional places and see the egyptian culture there are two amazing restaurants there 1- tom and basal ( for koshare food ) 2- om hassan ( egyptian food )  th go around cairo by taxi i advice you not to believe the drivers about the price for example if they said 100 say no i&#39;ll give you 40 only because they are usually multiplying the cost by 3 4 or 5 times or try to use the smart phone application for taxi like ( uber and easy taxi )
18 1453235412
lots of traffic and pollutionbut i love the energy and dynamics of the people always bargain and never take taxi from the street i recommend you buy a local mobile line and use uber or careem taxi service it&#39;s safer and more accurate using gps people are ready to help but be careful if on your first visit i urge you to hire a local guide     dont miss mohamed ali tawfik palace or al manial palace from the ottoman period sequoia &amp; left bank on the nile in zamalek serve great food faloka ride in nile is a must on a sunny day or at sunset riverside zamalek and cairo jazz club in mohandeseen have nice live bands on weekdays
88 1453239761
very nice
2 1453295852
i was able to go for a day trip to alexandria but i wish my stay was longer to explore luxor sharm el sheikh etc
94 1453354441
pyramids and museum thats tge best thing in cairo
69 1453361259
cairo is a city of wonders no doubt but in my fifth visit i can say it&#39;s becoming annoying and stressful more and more
36 1453382617
warm and welcoming restaurants and bars
6 1453388399
worth going to the pyramids just to say you&#39;ve been although it was a bit of an ordeal the museum is over rated poorly organised and filthy cairo itself is an awful place needlessly dirty and the people are very rude you can&#39;t trust anyone not to try and screw you for money no matter what they say or how friendly they seem there is always an angle to get you to a shop or a tour or something they will get commission if you spend even inside the airport where you would assume you are arranging things with reputable agents make sure you clarify every tiny thing that is or isn&#39;t included in the price personally if i were to go to cairo again i would not stay in the city (which is a shame as the windsor hotel was interesting and quirky) i would arrange with my hotel to pick up at the airport and i would book all and any trips online before i went
33 1453425169
the pyramids and sphinx are truly mind boggling and magnificent i feel very honoured to of had the opportunity to see them  it was fascinating to see all the wonderful treasures in the egyptian museumand at the time i was in cairowhich was 22nd-24th decembervisitors were allowed to take pictures inside the museumwhich is not usually permittedbut i believe that privilege ended on the 7th januaryaccording to my excellent guide ahmed  unfortunately i don&#39;t know his surnamebut i had a enlightening and enjoyable private tour around lots of places in cairo with himand would recommend it to anyone  the driving in cairo is bonkersa real eye openerbut you might be safer closing your eyeswhen crossing the roadsas people and vehicles just seem to come from every direction and angle!
5 1453439776
i like everything  lovely nice people  cheap prices  good weather
64 1453616294
cairo is a modern city but you feel the heritage and authenticity right away it is a vibrant city that honestly never sleeps and you can always find someone that would want to hang out any time of the day there is a lot of places to go and a lot of places to visit and also a lot of unbelievable beaches to have fun in try to hook up with a local and you will be set for the trip of a life-time
67 1453623236
we visit pyramids museum and zoo and city tour it was really nice food was good we they have selections of grills with fresh arabic bread also its not expensive
58 1453636800
my trip was a business trip i stayed to meet officials there was no touristic visit
71 1453709347
i feel only traffic bad
58 1453735085
highlight were the pyramids    for food there was good falafel place near the hotel    better to use hotel taxi to go around as it is much safer other option is to use viator tours which are also good    avoid traffic if i can!!
90 1453746762
the pyramids and sphinx are amazing  worth going back a second time to take it all in as the first visit can be a bit overwhelming  touts will hassle and it is easy to go into automatic &#39;no&#39; mode but it&#39;s good to say &#39;yes&#39; sometimes  ali works near the second pyramid (khafre) with his camel balool  ali speaks good english is very friendly and informative and he knows how to use a camera for those shots of yourself sitting on a camel with the pyramids in the background  balool is a sweet-natured animal  who has been well-trained by ali not to spit or bite  ali sticks to the agreed price and is a very good guide    the egyptian museum is well-worth a visit and it&#39;s easy to spend half a day there  the death mask of king tut is really beautiful - the most impressive piece in an impressive collection    i thought the citadel was a bit ho hum but it does have very good views of the city     good egyptian coffee and cappuccino at the second coffee shop across the road and to the right of the museum a nice place to watch people and maybe write some postcards  they still have them in cairo    taxi driver eid is a nice old man who sits patiently outside mena house hotel waiting for customers  he&#39;s not at all pushy so you may not notice him but he knows cairo speaks good english and he&#39;s a calm and patient driver  his number is 00201229485875  if you call him he will pick you up from your hotel or the airport
88 1453818020
i like every thing
94 1453879556
citystars mall  yes  rush hour
22 1453884372
as a solo female traveler i felt very safe and would recommend tourists to stay within the downtown cairo area for ease of access to metro stations as well as various mini buses its easier to commute by metro train as cairo is an over populated crowded city therefore sometimes going by taxi or bus takes longer as you will end up being stuck in traffic there is no certain rush hour time in cairo as a crowded city full of people it tends to be high traffic in streets almost the whole day regardless of time frametry the local egyptian dishes
5 1453907765
cairo is great city go and see the egyptian museum the pyramids make the nile cruse the khan al khalili the islamic and cristian historic site the azhar park and more
67 1466034173
it&#39;s alful place in general however in some circumstances it can be interesting - monuments and historic places are great but infrastructure just terrible
52 1466074893
i have been here many times to cairo and have work commitment here the place is great to visit and one should not miss it egypt is loaded with historical places and every corner here tells a story
80 1466084516
i would not stay any longer than 3 days in cairo the main things to do is to see the museum  and the pyramids which could  be done in 3 days i personally  next time would choose a resort hotel near the pyramids so it gives you more of an opportunity  to relax by the pool and enjoy the weather when your not out sightseeing
45 1468023295
“horrible stay at fairmont nile city!!”    i booked 3+ months earlier to my stay using my reputable multinational corporate rate &amp; was in contact with the reservation team before i arrive    i checked in on july 3 where as accustomed the reception checked my identification &amp; took an a4 copy of my saudi arabian passport on july 5 right 1 hour before the iftar time of the last day of ramadan the reception rang my room while i was napping immediately as i picked up ahmed samir from front desk asked me if i hold the egyptian nationality&#47; residency i answered no he then said &quot;you will be charged extra amount for your stay because you were offered the egyptian stay rate although you are not egyptian&quot; and went on arguing with me with complete rudeness raising his voice saying that the hotel has a corporate agreement with my company offering egyptians different rate than other nationalities &amp; i will be paying more money to cover the difference i tried to explain that i am unaware of this agreement it was clear in my work email i used to book that i work in saudi arabia business unit the reservation team did not check my nationality when i inquired about the corporate rate and i have an official reservation confirmation with the given rate  ahmed samir continued with utter offence accusing me hinting that i deliberately deluded the reservation team to get the egyptian rate!! and even if it was the hotel mistake not to check my nationality before offering me i should pay for this mistake!!etc!    to make a long story short throughout my 4 nights stay the front desk refused to refund me the extra down payment i have paid at checked in claiming that i owe the hotel extra money!! etcthey even demanded that i show a proof of the reservation confirmation with the rate i was offered almost calling me liar!  not until i showed and forwarded the confirmation email twice! that anyone cared to respond to me-- everyone i spoke to at the reception tried to avoid giving me a direct answer and said they will check with johnathan the sales director who since it was eid holiday is away and having his mobile off and no one knows if he will ever respond!!    not until the night of my check out i entered my room late night on july 7 to find a careless message from the reception with one blunt line: &quot;kindly be informed that the room rate will remain the same as it was before&quot; without even offering their apology for the inconvenience the least!-- not to mention the abusive disrespectful and indecent manner i was treated!!     in short this is an extremely disappointing hotel with vulgar unprofessional and untrained staff who would not hesitate to offend the guests and treat them with zero respect  fairmont nile city hotel made sure to ruin my visit to cairo my eid holiday and the reputation of fairmont hotels all together and i will make sure never to return to this horrible hotel ever again!
69 1468093057
overall a great place to visit so much history make sure to use uber so you are not cheated by taxis
63 1468135709
nice for going around shoppi g great museum
37 1468144541
cairo is a good places but the egyptians were very much asking for tips  with no shy all the way and every place you go
91 1468145310
the pyramids and sphinx in giza were amazing as was the egyptian museum i would suggest to get a guide as they will talk to you all about the egyptology and also make sure that you do not pay over the top prices at the attractions  i would not recommend as a solo woman traveler as the egyptians are known for hassling tourists which becomes a little tedious after a while
60 1468147525
nice city you can enjoy a various of histrocial places there
37 1468152968
sadly very dirty unorganised overcrowded and way too many people expecting a tip while doing nothing for it    the flip side is that most egyptians are nice people who care about what others think of them and try to please    the driving is terrifying and dangerous
19 1467191212
i love egypt and cairo and the egyptian brothers
92 1467271071
very safe and cheap! super friendly people!
57 1467288104
cairo has me smiling every time i visit if you want touristy stuff and culture you got it if you want to get to know the people you got it if you want malls and brand shopping you got it    the weather is mostly nice and not too hot     uber makes sure you move around safely and fyi it is indeed cheaper than taxis (even a trip to the airport cost me 95le rather than the 150le i had paid a taxi previously) theres a cash option for cairo uber    i know many people sometimes face ripoffs but everyone i met was kind welcoming and helpful maybe because i look egyptian and speak the language
43 1467294305
pizza hut mc domalds pyramids train to luxor avoid over priced taxi
8 1467299325
crazy traffic and public transport
25 1461556269
it is a good place to visit
70 1461568941
dont go there go to sharm el sheikh
28 1461569115
people are nice
33 1461577432
good city  crowdy
44 1461577870
i went to several places and had nice food  in cairo you are never solo people are friendly and helpful
66 1461594959
very crowded
42 1461599616
amazing city - amazing people - i love cairo
91 1461650136
we were mainly there to visit family so that took up most of our time it is a shame that there aren&#39;t more tourists because egypt needs them to come back!!
55 1461660091
the best place having diversity in activities &amp; nice weather allover the year
5 1461693810
nothing better than the great pyramids however the nile cruise old cairo and citadel were also highly appealing
0 1461783144
great city and great people
94 1461793538
it was a business trip and city is peaceful now and uper is working in cairo
91 1461810296
i love the pyramids it&#39;s amazing i&#39;m not really a flexible person most of the time i ate fast foods cairo is a big and interesting city but for the woman who travel solo shouldn&#39;t hang around alone you can meet any unwanted attentions  beside of seeing the great pyramids &amp; museum i went to malls for shopping have coffee at my free time at night i have dinner &amp; drinks at the hotel pool side
68 1466946978
friendly people and lots to see
65 1466951394
the sun and friendly people the only horror is the traffic!
81 1457601125
ancient landmark such as pyramid and  sphinz in giza are nowadays surrounded by  shops and hotels i was here in 2001 and remember how vast the desert area was the security is very tight especially to tourist hence it is recommended to be accompanied by local guide    halal food is easily obtained here i enjoyed fruit juice especially mango and banana juice    for shoppers do include asfour crystal in your list if  pendant light is not to your favour you may opt bracelets or rings for yourself or souviniers    bazaar el khalili is awesome at night! get your arbaya or souviniers here!
48 1457608037
- the best places are  pyramids &amp; cairo museum     - the best  place for families with young child is dream park &amp;  pharonic village
82 1457617153
crazy traffic smog and very slow to do business  tourist traps a rip off
44 1457623499
view of river nile warm weather friendly people cheap prices museums
79 1457632327
although i was on a business trip and working in cairo for over 2 weeks i did get the chance to visit the pyramids at giza this was a super trip and enhanced by the informative guide that escorted me the traffic in cairo is very heavy and keeping to a timetable can be tricky so always allow plenty of time for your journeys the people in cairo are very friendly and made me very welcome!
92 1457636825
i love the hustle and bustle of cairo the traffic is amazing to watch and crossing the road is an experience in itself - a bit like a white knuckle ride at a theme park!  everyone is friendly and helpful - but don&#39;t ask for directions you&#39;ll get a different answer from everyone you ask and none of them are likely to be correct! if you know where you&#39;re going the underground metro is the fastest cleanest and cheapest method of transport - although in a taxi you&#39;ll see some amazing sights and sounds ;-)  if the taxi doesn&#39;t have a (working) meter agree a price before getting in especially as a foreigner the local restaurants are cheap and the food is tasty - find a place that&#39;s busy and you won&#39;t go far wrong cairo seems to be a city that never sleeps - the roads begin to fill up before dawn and well after midnight the city is still full of life
87 1457640221
if you come to cairo look for a hotel near the pyramids enjoy the majestic view   living in the city is just not worth it
63 1457717778
great peoplepyramids museum sshopping delicious oriental foodnot expensive at all
60 1457783782
nice historical city  pharaonic christianity  islamic &amp; with feature of modern life  the peoples are very friendly  the food are so tasty
29 1457821350
walk beside tge nile river i ate @ abou shakre paul abou al seed  swiss restaurant and sofi it was really easy to get around cairo as my friend recommende for me a tour guide with his car he took me to very nice places and he was kind kowledgeable and hard working person his name tamer hamdi and his number is +201117044603 just in case any one want to contact him i really enjoyed the trip on the nile pyramids etc go to the east go to the west egypt is the best god bless eygpt ☺
32 1457857250
alhussain dist
39 1457867609
egyptian museum giza pyramids and sights old cairo and environs friendly people fascinating street life
71 1457873812
i have been living in riyadh saudi arabia for the past seven months which is one of the most boring lifeless places on the planet so by comparison cairo is a living breathing very dirty polluted chaotic but vibrant city i loved the constant noise crazy traffic beggars small shops random architecture and friendly people of this city however as a blond blue eyed woman i get better service than the average joe i imagine another plus--in america i get the occasional rude comment about my weight but in egypt i am considered a beauty plus size girls come to cairo if you want to feel like a movie star
30 1457880173
nice place to visit!
89 1466546830
do not believe any taxi driver when they tell you puramids are closed museum are closed they wil approach u with a very welcoming attitude and ask u ur destination then they wil say it is closed and try to suggest some other place they always overcharge none of them is honest metro is cheap and easy to get around the city is dirty with rubbish everywhere on the streets and crowded people wil always ask for tipsexample: short camel ride with photo at pyramids they wil ask u to give how much u&#39;d like to  but at the end they wil ask for 100 egyptian pounds and they wil not leave until u bargain the amount be aware of these people ask the policemen if u have any doubts or the ladies on the street the ladies are more helpful than men  in conclusion not a comfortable city dishonest people everywhere
83 1466590883
cairo is cairo - a unique place that offers ancient places to visit alongside a busy frantic large sprawling city there is no obvious &quot;centre&quot; other than tahrir square and the roads are insane with traffic
84 1466624625
visit to exiting places
28 1466679157
scary place
13 1466684958
im moving there
53 1466687363
i cannot say enough about this city i loved cairo there is so much happening and there is so much to see  i would have liked to stay longer to really get a feel for the city and the culture when you visit be prepared for people trying to sell you things this is common there before you go make sure you know a little but of arabic it will truly help you during your stay have fun and explore the city there are little hidden gems everywhere
35 1466705368
this place is a living museum! the cross section of a variety of cultures history art etc will amaze and delight you a vibrant city with much to offer the adventurous traveler
75 1460467335
great historical ancient quarter but left extremely dirty
68 1460499514
safe and welcoming the egyptian people have great hearts and make you feel like family
87 1460504163
lots of history culture and even modern shopping in modern malls
31 1460534407
cairo is a great destination as people are helpful and sweet they go out of thier way to ensure u have a good time lot of respect and love for indians
13 1460545273
i don&#39;t advice anyone to go to cairo especially women should think twice
13 1460618517
very nice place
47 1460634375
downtown cairo is amazing and the people are friendly lots of police so you feel very safe at all times
79 1460642105
nice destination and kind people
79 1460642712
sequoia and the platform are amazing places to eat in and have a chicha on the nile
99 1460644475
great city
55 1460644665
best city
28 1460654822
the best city ever for tourism
71 1460655438
please dont let anyone assist youit cost&#39;s money and they always mislead you not a good thing for you to do
97 1460710059
would love to visit again
30 1460711084
not very clean
7 1460714877
best
81 1460729556
the pyramids in giza are a must in anyone&#39;s &quot;bucket list&quot; they are truly amazing sakarah another ancient site should be a must for any person interested in ancient cultures the bazaar and coptic cairo are quite a site but not for the light of heart cairo has 19 million people and with that comes noise traffic pollution etc it is not easy to get around cairo there are so many places of interest and places to have a break like the mena house a gem of a place right across the pyramids a respite from the crowds and heat  there&#39;s a lot to do and things to see i would recommend cairo to anyone with an adventuresome soul and a young heart people are very friendly but once again not an &quot;easy&quot; place to be
59 1460729853
pyramids  citadel of saladin alazhar university
84 1460881918
no best only bad thinking this city only demanding money everyone only wanted money
70 1460885433
ofcourse a great city with lots of things to do if you are a fan of history you will love it try avoiding the local taxis  use uber ; better cars  you know the fare estimation and a driver which can be traced later
74 1460886874
can be better
51 1465237472
traffic and smog  a downer  but the ancient kemet monuments to the west and south  worth any obstacle  we visited the giza pyramids and stayed there as all the old and middle kingdom tombs  and some new kingdom as well are very close      we saw 23 pyramids in all  meidum  daishur  el list  abusir  saqqara  and 27 tombs  when you thind tat these were built 1200 years before ramesses ii rule  then the monuments in the south are a further 1 500 older  the mind boggles      we do not do tours  hire a driver and car  older ones ( both ) best  we spent 10 hours out in the 50 degree heat at our own pace  got great sunset photos at daishur of 3 remarkedly different shaped pyramids
97 1465247305
excellent city and so safe as wellthe people there are so helpful and kind as well as alone traveller i was able to get out at night although i was appropriately dressed which did help as well so did not get alot of attention  i tried to see the islamic art museum but this was closed for renovation but have been told by a person who works there that it would open after ramadam 2016  getting around cairo was interesting and on the most part i used the white taxi&#39;s you do have to be aware that they will charge a tourist extra and so i resorted to using the uber app on my phone and then i knew how much was being spent and the money came direct from my bank account instead  because it was so hot when i was there i tended to have light food and the costa coffee place in el dokki was brilliant for this there are other good places to eat as well  for shopping i loved the khan el khalil souk but please do be aware that you will get alot of pestering to buy this and that the local market is brilliant as well and you can get some very good bargains as well there
72 1465264358
egypt is simply amazing &amp; incredible! filled with kind and humble people my stay at egypt was definitely pleasant and i wish it could be much longer 15 days is just not enough!!! :)
47 1465317780
good city
19 1465324078
fatimid cairois wow  pyramids in guiza  museum is great  eat in naguib mahfouz cafekhan el khalili  cabs are cheap and handy
51 1465365053
na
41 1465394713
it is a lovely big city and you can enjoy yourself but you have got to be careful with the food cleanness and the places you chose to eat and visit as there are poor and rich places the both places have so much to see and shop but again u need to know the right price to pay them as they could ask you for the double price  lovely and friendly people and you can get around easily as taxis and other transports are cheap but so much traffic and busy
71 1465395684
if you don&#39;t mind the hot weather and the bad driving then cairo has a lot for you from the pyramids to the museum khan el khalili to the nile there is lots to see one thing that puts you off is the traffic but its worth it
50 1465423272
it&#39;s cheap now many places to visit nice people
45 1464716068
it is absolutely beautiful city i recommend everybody to get advantage of the good price trip and enjoy the beauty of cairo ;museum pyramids the people nice food and very good value for money
2 1464727810
if i had to give one recommendation use uber instead of the local taxis most taxi drivers take advantage of foreigners and will charge double
24 1464766905
noisy dirty and busy  the roads and driving is erratic and unsafe
74 1464776208
a city of history and pleasure
3 1464777807
stunning amazing everybody can find best place - from pyramids fatimids and old cairo museums shopping in oriental bazars or modern shopping centers enjoying biggest city in africa in crowded streets or in relaxing azhar park
96 1464842412
القاهره جميله بناسها الطيبين وعشت فيها مده طويله وكانت زيارتي جميله ورائعه زرت الاصدقاء القدامى وبعض الاقارب وزرت الاماكن الجميله وشكرا لسائق السياره محمد وشركة تأجير السيارات والبلد جميله وتستحق الزياره
39 1464866377
fabulous city you should go
86 1464877889
nice
24 1464884386
pyramids  museum  shopping
86 1464944857
good
53 1464947348
it was business  so did not get around to see the sights
53 1464955357
museum   sphinx
28 1464956949
a very busy chaotic city - a lisenced guide is a must!
96 1456867001
cairo
46 1456934402
tabboulah restaurant was brilliant20 minutes walk from the hotel zamalek is a reasonable place to spend an evening with decent cafes and local shops    two oases of calm are the amercan university bookstore and kaffeine cafe    taxis are cheap and plentiful (negotiate though)    otherwise downtown cairo is a menacing hellhole the pollution makes mumbai seem like the alps for air quality expect constant hassle on the streets (never really threatening) a heavyweight military presence yet more hassle (eg bogus guides in museums) and yet more pollution     cairenes already live in a city choked by traffic funes and cigarette smoke but live to burn anything they can find in the streets they are generally a friendly lot though (if you&#39;re a guy)    after talking to others i didn&#39;t bother with the valley of the kings or alexandria asguess what? you can&#39;t move for hustlers     if you have to go then speak an uncommon language and they leave you alone pretty quickly my basic greek worked a treat otherwise try clingon or confidently speak nonsense     if you&#39;re expecting the romance of old egypt you&#39;re out of luck you&#39;re more likely to share a few beers with a yeti in the himalayas than experience raiders of the lost ark
2 1456997486
best places in cairo are those called fatimist  khan al khalil  al hussein moez street saladin citadel sultan hassan and refaii mosques church&#39;s  pyramids for sure and city stars mall as well arabia mall
77 1457057549
i advice everyone to not visit the giza pyramids khan al khalili cairo tower those places are not safe to visit at all the police are not doing anything over there the miss behavior with all visitor are coming from different countries
13 1457078690
i loved the history of cairo  tours arranged were brilliant very easy to get around i found the negotiating very tiresome
6 1457084268
-lebanese restaurant next to nile it was great  -too much traffic on  the roads
73 1457084955
cairo is a great place to visit it has a lot of attractions within the heart of the city and around it however it you&#39;re planning to visit egypt don&#39;t stay for more than a week in cairo and try to visit other status like alex or whatever i feel one week is enough to go everywhere around cairo without getting bored of visiting the attractions multiple times
86 1457101845
enjoy your diffierent life
13 1457174865
love the street food at the boat for dinner at nile river and after the pyramid tour walk around cairo by foot you will be surprised solo travellers should be friendly and start the conversation
14 1457180237
it was beautiful we would spend the entire day out there were so much to do and so many places to visit
2 1457180256
it is a kind of heaven
52 1457187249
we had a private driver so getting around was easy  museum is a mustour driver knew lots of eating places which were value for money and served delicious food
1 1457203801
cairo museum amazing i just love walking around trying not to get flattened by crazy trafficjust avoid being enticed into shops if you don&#39;t actually want to buy
46 1457213663
feel the historical life
6 1457242523
the old city friendly people everything was good
82 1465478823
xx
44 1465498621
it&#39;s good for the history  the area around the opera house is good  but the oppressive military presence - guns and barbed wire - means we just wanted to leave as soon as possible
85 1465541458
terrible traffic and dirty streets but the pyramids are amazing
38 1465554661
cairo has tremendous number of places to visit just the cairo museum alone requires a full day! there are huge mosques churches and synagogues that deserve a visit additionally there are other sites around the city and in the greater cairo area that are worthy    i must say that a week will be suitable to be able to discover most in and around cairo    please make sure that you bring your mosquito repellent to avoid being bombarded by big mosquitoes i was torn apart by a couple only and a week after returning from the visit am still recovering from those bites    please don&#39;t be surprised if you see no marking for separate lanes on the roads there are no traffic lanes and sometimes on a busy one way you find people driving exactly in the opposite side:meaning they are streaming towards you: interesting and crazy!!!
7 1465570874
you should visit the pyramids and for shopping do not miss city stars mall
64 1465579804
culture busyness people atmosphere - it is all fab
33 1465655065
the weather is great but taxis and fruit sellers are a night mare not regulated  they should set every taxi with a compulsory meter that is clearly showing you every penny and they should be stopped and checked government should do more    many beggars it is so annoying if you sit in a cafe or a local restaurant sitting off the premises  government should do more     the metro is great and easy to use but you can&#39;t get other public transportyou wouldn&#39;t know where to catch them and how to pay
87 1465658370
an amazing city with beautiful people and a very rich culture
38 1465673925
there is nothing in cairo to see  a dry dusty uncolored city  except the pyramids and the museum getting a taxi and negotiate with them is exhausting everybody want to rip you off
2 1465705674
there can be lots of traffic especially early afternoon so leave early and take your time exploring markets coptic area gizeh plateau and its  pyramids  the cairo museum deserves at least 3 hours and in most other parts of the world would make up 30 or 40 museumsthe collection being so richa must see  driving along the city of the dead is fascinating as well but entering is forbidden
20 1465720250
i love cairo and zamalk is my new passion
92 1465726753
hot dusty and polluted   the pyramids and museums were wonderful  the people are sly thieves all they want is money money money what a complete joke  don&#39;t trust the taxi drivers they&#39;re money hungry and lie through their teeth
45 1468529177
cairo is nice country
2 1468576395
a historical place offers shopping extravaganza and nile river offers good activities
7 1468665771
loads of history to learn
83 1468668870
every thing is beautiful
80 1468678760
i was there for business and had no time for leisure
77 1458897221
المقطم  القناطر  مطعم صبحي لازم يكون معاك واحد دليل سياحي من اهل البلد  النيل
89 1459068322
this time was just a transit on the way to sharm el sheikh have a map at hand move in the early morning preferably after dawn
1 1459072242
lots of sights and wonderful architecture especially churches    the people were unfortunately trying to take advantage of us and selling us things at very high prices    dress modestly because skin is an eye-catcher and some locals my harass :(
34 1459072802
2 days in cairo is too much spend more time in other cities
6 1459073450
it was extremely safe everyone was so welcoming the hotel luxurious and so much to see and do
29 1459078141
very safe with really friendly people amazing food and crazy traffic (felt right at home as an indian :) ) the attractions are just mind blowing and it was just as if i&#39;d stepped into a page out of a history book
92 1459087681
egypt was amazing as usuals
40 1459088332
trip to pyramids by camel and the museum were both an experience to be remembered    1  avoid the guides at the museum they will cheat you out of your last cent!  2  there was only one that did not invite me for a hospitality drink (tea) but was honest - he asked how can he take my money from me   3  as a tourist in cairo you are considered a bottomless source of cash  beware
75 1459093041
very diversified city full of history from different ages great civilization with lots of monuments for pharoescopticislamic culture
38 1459107760
it was a sensory experiencethe food the buildings the traffic the people the colours the sightswalking around downtown and sampling street food  good walking shoes sun hat water bottle and sunscreen and you are all setit is an amazing metropolis!  make sure you have small bills - as they are needed as tips to supplement the pay of many workers around egyptfrom bathroom attendants bag handlers drivers tour guides tomb&#47;temple attendants
1 1459108336
its an amazing city full of sensory experiences
13 1459112838
take the subway more
22 1459191779
you can read and see all the photos of my trip here:   http:&#47;&#47;wwwkeepcalmandwandercom&#47;tag&#47;egypt&#47;
76 1467653070
magical place for pleasure and business people friendly and helpful
56 1467710388
nile city
65 1453976404
maadi  fast food  only with subway  i will avoid the same hotel i choose that time
85 1453979885
cairo is very different city nobody sleep early and the people are friendly  at night cairo is very charming and you can go to downtown sure you will enjoy it
30 1453982115
hard to say if you really need a tour well you will need a guide and a driverin my opinion you don&#39;t need a guide if you know your history -i did more than some guides my advice is to hire a driver that speaks some level of english a driver and a car will be something you will appreciate in this crowded very pollluted and constant flow of people requesting something from you mind you the state of the economy and the effect of the terrorist attacks understand that some people only income is the tipping from tourists and for some it complements their meager salaries however it get annoying at a certain point i gave a lot of money away in tips because i felt embarrased -of what?! my japanese companions never gave tips and they were surprised of how money i was &quot;wasting&quot; but again they lack empathy i found the islamic cairo overated dirty and dilapidated i went to the synagogue in the old town and didn&#39;t dare to wear my kippah stagnant water and garbage sitting in a pile on a wall of the temple the felluca boat was really bad at least the one i tookeverybody wants a tip and everybody has 3 or 4 children so please help me!  the cairo museum please read other reviews about it very old and lack many labels i was lucky that some exhibitions had labels written in french or german but for those who speak only englishthey were allowing people to use their cameras without flash yet there were the usual ignorant flashing light everywhere with their cheap cameras or phones this is new and for a short time check with other people oh my guide didn&#39;t know about it how come? i read first in tripadvisor
80 1453992584
must try abou el sid reastaurant to sample egyptian cuisine bamia moloheya babaganoush tahini mix grill kofta
84 1454026669
ابحث عن تطبيق كريم في هاتفك شركة اجرة  اسعارها ثابته ومعروفة
13 1454059358
see as many pyramids as you can there is more than just giza the cairo museum is a must take your time soak it in or it won&#39;t feel real
88 1454064015
it was wonderful seeing the pyramids and various other places of interest
91 1454092841
your trip is incomplete without visiting these places i strongly recommend    cairo museum the citadel al azar islamic cairo khan al khalil
55 1454133634
i am egyptian and proud to be! i love egypt and every single detail of my country i know for sure that all egyptians do adore their country  however i think we need to work harder to position our country to the right standard it deserves god bless egypt and all egyptians
38 1454153996
airport security in need of attention
3 1454174735
cairo is one of the most beautiful cities  it should be traveled more than one time a year
75 1454211003
shopping specially  tailoring leather jackets in the small  shop i visited  hand made walt and bages  they have great prices  so many places to visit
35 1454243799
i must avoid the trafique it was a lot between the airport and the four season hotel
6 1454321330
the situation of the hotel is excellent just a few minutes to all the amazing places like the museum river nile pyramids and lots of other interesting places and restaurants i&#39;m still buzzing from my trip and can&#39;t wait to go back
45 1454330548
old cairo with all the mosques and castles assouan with the river nile cruise and all the relaxing and positive energy on earth  river nile walks at dusk    the magic of the pyramids  it was amazing   sahara safari and oasis adventures   very nice well priced food   i would avoid moving around during rush hour
77 1454401314
cairo is nice but giza is very unorganized and dirty traffic is just insane so don&#39;t be surprised if your ride comes slope to hitting other cars in the way to the hotel or to the sites people are friendly but vendors are too intense most of the time you have to say no too many times better get ready for that but in overall is safe and very interested culture with all its old arqueological sites and monuments the museum is a must see
87 1454411630
the most bad thing the weather there is too much carbon in the air  you cant breath :( ، also if you dont like old looking bulidings then go somewhere else
43 1454498269
pyramid of giza was the highlight as well as the museum and other spots
92 1454515085
con games are constant and allowed to run unchecked in all tourist sites especially the pyramids and at the airport   this should be a destination only attempted after experiencing at least a dozen other countries and developing a strong personnel level of experience in handling foreign cultures
43 1454561209
loved giza loved the nile in cairo surrounding cairo is dirty and crowded people were pushy and deceiving literally painted our names onto their paintings to guilt us into buying we walked away feeling we had been taken advantage of
54 1454583918
nice weather and helpful people
26 1454587808
visit the pyramids cairo book exhibition shoppingnile cruise old islamic cairothe citadel and if you have time visit luxour aswan sharm el sheikh i mostly ate in the hotel and it&#39;s easy to get sound cairo
14 1454614867
amazing is the least which i can say  it&#39;s just the sense of life and beauty :)
60 1454632086
the problem with cairo or the good of cairo all depends on your time if you are the casual traveler who flies in to see giza and the pyramids or the egyptian museum then you have not seen cairo at all  if you are there on business then you do not see cairo too  if you are on a tour you never see cairo  i travel independently  being there on one&#39;s own can be interesting but frustrating  we could speak of dirt  we could speak about horns honking  we can also say we can cross the street without being hit which perhaps cannot happen in many other parts of the world too  cairo is intriquing  i loved and hated cairo    traveling on the trains first class costs 40% more  yet in dollars is very cheap  seats are more comfortable and reserved  there is no heat on the train  it would be wonderful to have it in january  i bet you feel egypt is hot all the time guess again  i could write more on egypt but i am not being paid for these thoughts which this website just hopes i will keep writing and writing hope to have helped some people  all the best to you travelers if you are on tour then your fate has been decided before you even paid your bill  good luck
94 1454663415
cairo is history  pyramids nile  museums     it needs to be more organised and more clean
24 1454666836
pyramids
2 1454757209
the egyptian museum  el moaaz street  el saida zainb  enbn tolon mosque  mohamed aly castle  downtown is very great place to walk and know more about egyptians life style
92 1454757493
i like the hotel very much staff  location  room  view  services really its first time ti see hotel arrange free 5 star bus with tour guide to visit famous places in egypt like pyramid  cairo castle  cairo  museum daily for free   arrange free transportation from and to airport free of charge
29 1454780288
like nile cruise with late dinner very much and the zoo also    dislike the crowds and congestion every where
47 1454821545
pyramids
67 1454824350
bargain for everything and start at 10% of asking price
61 1454835037
cairo is a great city but is polluted and dirty i believe it is a matter of time before things start improving
3 1454853772
air polluted roads were not clean lack of traffic law and ect
93 1454860420
my favorite places were khalli market old cairo market and religious community in cairo it&#39;s not very easy to get around in cairo for those who don&#39;t know arabic but it is manageable i would like to avoid the high tourist ticket fares to the tourist places and pushy tourist souvenir sellers
39 1454865045
pyramids and the cairo museum are great local food ok but not an adventure eater
65 1454874464
i will do my best to avoid taxi drive
34 1454909610
pyramids visit
3 1454919350
my main attraction to cairo was the pyramids of giza like most other tourists the pyramids are amazing but if you plan to go down inside them be prepared to make a steep very tight squatting walk down a small hot tunnel wear good footwear     what i didn&#39;t like away from the pyramids cairo falls apart streets are littered with litter and dirt seems a good percentage of the population on the street (especially shop owners) latch on to you because they can see you&#39;re a tourist then offer you things as gifts then expect money in return  people ask you &quot;where&#39;re you from!?&quot; they say they love any country you answer with (trust me i said multiple different countries) and then they ask &quot;where you going!?&quot; you answer for example &quot;the museum&quot; they then proceed to walk with you directing you to the museum but not all the way just around 100-200 yards down the street even if you didn&#39;t ask for directions then expect money in return this never worked with me as i&#39;m 6&#39;2 and well built so i had the courage to say no over and over however i can see how it would be very intimidating for other people especially when one man wouldn&#39;t let go of my hand and was asking for money! cairo airport is like time travelling back to the 1960&#39;s but not in a good way if you&#39;re planning to go to visit the pyramids then stay in a stunning hotel which has nice rooms and a nice restaurant i wouldn&#39;t recommend walking around in cairo as even my hotel staff told me to lock my backpack up tight and be careful not everyone was as ignorant invasive and rude as the people i mentioned above i met several people (other than the hotel staff) that were very nice and helpful
9 1454929016
didn&#39;t like the city at allvery dirty and full of filth people are there just to take money from the tourists cars were so dirty and in run down state
90 1454956744
be prepared to be robed by everyone all the time
62 1455008131
city of cairo is of course a must see but it is a little too fast paced for me i really enjoyed my time at the pyramids loft in giza as you could find whatever pace you were looking for much easier when in downtown cairo take a boat ride on the nile find someone with a small motor boat over the larger boats for a more personal experience also if you are trying to go to the antiquities museum decide to go there and don&#39;t be convinced of anything anyone tells you otherwise there are no &quot;better times&quot; or &quot;times to avoid tourist groups&quot; there is a museum that has hours of operation they want to divert you to a friends shop where you will spend money for an hour while you wait for the tourist groups to leavebut they will be there even if you wait the really nice guy that tells you about this will also be a &quot;professor at the university&quot; most likely a language professoryou&#39;ll see lol
32 1455011560
it is a great very live city to be at very nice night time worthy great museums to visit huge shopping malls metro stations are joining almost every part of the city a lot of taxis are available
27 1455014174
cairo tower  the pyramidskhan elkhalili elmoez street alazhar park muhammed aly citadel the egyptian museum nile cruise and cairo festival city
99 1455016113
it&#39;s a big rubbish tip king tut would turn over in his grave if he visited cairo now
48 1455025209
great city with lots of entertainment   very noisy and lots of beggars you have to be always careful of your belongings  don&#39;t drive thereget a taxi    the passport department and luggage at the airport is a nightmare you will need a lot of patience as everyone will try to squeeze in front and they have no idea what standing in a line is also everyone will try to sell you something    the tourist attraction are great boat cruises on the nile with dinner casinos restaurants and dance places are super
57 1455027085
old city&#39;s
24 1455027746
egypt is lovely but the people there are psychos  they try to take each and every penny from your pocket whether you like it or not  never use public cabs and always buy stuff with fixed prices  always know where are you located  never go to a cultural place because they will try to mock you when they find you a foreigner i can&#39;t deny that there were good places like for example seagull restaurant on the nile  also city stars was a cool mall which includes everything  if you want to see egypt from above i would suggest you cairo tower or alazhar park  but again my advice to you is simple  never go there alone  try to have an agency or communicate with someone who is living there
7 1455043085
cairo books fare otis difficult to go around   also  avoid to deal with taxi drivers are
96 1462875842
new suburbs are clean and somehow organized     the city is buzzing with action 24&#47;7 and weather was awesome!      history is great and potential is worth exploring
62 1462877677
it&#39;s a nice city with a lot of things to see unfortunately it&#39;s very dirty
68 1462880550
cairo was incredible wish i could have stayed longer! the bazaar was the best market i went to in egypt the people were lovely you could buy beautiful things and i felt very safe the whole time would thoroughly recommend!
3 1462881876
try sabai sabai restaurant it was good
72 1462938888
i loved traveling in cairo people are nice the city is a combination of modernness and history which is magical there are so much to do here sight seeings food bars etc
12 1463005612
cairo has an amazing history and that&#39;s about it just a pity the people are un-friendly and expect a tip for every single thing not easy to get around and the place is dirty and does not meet expectations
46 1463060458
city of culture  i love it thanks
92 1463064982
i liked the vibrancy ofthe city scenary specially the river view  i did not like the officer at the scanner of the departure lounge at cairo airport who asked for money despite wearing the officiall police uniform   i also did not like some of the taxi driver dodgy practices stick with meters if possible it work cheaper than agreeing a fair
71 1463075932
it was not the most pleasant trip the people in cairo do not welcome &quot;white&quot; tourists it is a very poor hot crowded and dirty place we were harassed by the locals for money most people stared and glared at us making us feel unwelcome most of the men leered at my wife like she was a piece of meat making us both feel uncomfortable and unsafe there was a riot in the streets that we almost got caught up in armoured trucks and police in riot gear were everywhere and going to see the pyramids was the worst experience of the entire trip it felt like the whole place is completely corrupt my fantasy of the &quot;egypt of my dreams&quot; was shattered on this trip it is a broken country in so many ways in my eyes i have done egypt and have no plans to return i will also suggest to friends and family to avoid travelling there
33 1463082288
nice place you should know well what to visit
31 1463688914
it is a huge metropolis (175m) traffic is an absolute chaos eating and taxis are as expensive as in western europe  but less than half the quality public transport is no go for educated egyptians leave alone non egyptians you will be served nicely if you tip well otherwise you are nackered no decent places to eat while on the go only in good hotels the city is full of antiquities spanning few civilisations from ancient egyptian pyramids up to the ottoman grand mosques the river nile is very beautiful at night from hotel terraces the political disturbances is reflected on security the squares and the important cross roads are like a military baracks armed vehiclesarmed personnels and barber wire are in sight your taxi will go through military and police check points every couple of miles egyptian citizens are generally pleasant and kind but the harsh economic pressures force them to grab any pennies  apart from site activities during the day staying in your hotel is safer  it is not clean city heaps of rubbish are everywhere   weather could be unkind during our stay in mid may the heat reached 46 degrees c for two days it was insane best visited in late autumn
32 1463693062
our visit to great pyramids left a bad taste in our mouth as we were fairly and squarely cheated out and the hotel driver told us it was a good deal i think he was joking and i could not recognise it!! i wonder what kind of commission he got!  hiring conveyance from outside was a better option met some very friendly drivers and charged us reasonably   a longer stay could have been better as cairo has many many tourist attractions  i think we shall have to arrange another visit  egyptian food was too bland for our spice spoilt palates   after we found this gem of a taxi driver getting around cairo was a pleasure if anyone needs address of this driver contact me at mansoor_husein@ hotmailcom!!!
20 1463709450
use common cense and it is a safe place to visit  for most the pyramids sphinx and cairo museum are a must  loved walking and wandering through various market area and shopping districts  found most people only too willing to help and advise of places to visit was here last 5 years ago  taxis are reasonable priced even due to the traffic issues  ate at various local cafes and found the food good quality  be careful not to exchange more of your money than required for egyptian hard to change back and pretty much no use once you leave
5 1463718702
amazingly cool city with uber friendly people and unbelievable cultural treasures one of the world&#39;s most underrated cities
21 1463746637
gizamuseum at tehreer square mosquesand what noteverything in egypt very cheap
97 1463829761
i travelled for businees i didn&#39;t have any time to enjoy trips
75 1463853571
very interesting but much noise
62 1463911686
cairo never sleeps very vibrant retro
76 1463919000
i like the food people and touring in old places and set in traditional cafe around
58 1463946481
recommended for one time  visit atleast for word travellers
16 1463950999
people are friendly food is delicious nightlife is amazing
32 1463989040
it has a lot of places for shopping and clubbing as well as the historical and oriental places
89 1463990229
its a great city bustling and full of 24 life
7 1455912634
pyramidsandrea resturant@new gizanodriving
30 1455977129
visiting the pyramids four seasons hotel on the nile and the egyptian museum
88 1455999897
getting worse reflecting political and social situation city is very dirty people stop to care
37 1456017134
good luck
44 1456044673
cairo museum is a must do  might be better to go by yourself and spend a whole day there  tours are usually combined with churchs and mosques which takes away half of a day
9 1456063660
best weather  food  hospitality   thx cairo
84 1456070941
mainly business
39 1456076716
starcity  cinema 👍🏻 but with limited movies (3 english 3 arabic)  khan elkhalili area   traditional area and good place to get gifts  nile tour  or even resturants along the nile  good place to spend time during night
29 1456113505
the authentic buildings the nile river the pyramids l sphinx and the hirsto of the place were the best thing i have ever seen in my  life !  i loved the local restaurant food   you can get around cairo easily with uber and cabs but don&#39;t get tricked ! tell them to turn on the meteor! some taxis would leave if you asked that many people run after you to let you buy stuff or ride horses and camels if they knew that you are a tourist! but you just have to pretend that they are not there
89 1456128241
the piramits are awsome once in a life time experience but overall the city was very dirty noisy no traffic rules garbage everywhere
40 1456129184
favorite places:    1  abu simbel temple  2 aswan and the nile cruise  3 karnak temple luxor  4 giza pyramid  5 khan khalilie bazaar    plan your travel and itinerary before you reach cairo
26 1456130248
perfect place
90 1456142626
it&#39;s easy to get around cairo
98 1456173343
cairo does not have much going for it it is crowded noisy and the air is horrible the egyptian museum is poorly lit and the signs are unreadable the only reason to stop here is to see the pyramids the rest of egypt is fabulous and safe
99 1456230048
cairo sucks just go to cairo 1 day and then luxor aswan 3 days for cruise and then hurghada and sharm el sheikh and alexandria 3 days each
92 1456238558
good friendly people outside the airport zone
76 1456240658
wonderful people! we found egyptians to be warm friendlyand helpful we were overwhelmed by the kindness and thoughtfulness we were shown   extremely polluted not great for asthma sufferers there is garbage everywhere! &amp; poole just chuck garbage out from their cars and houses no respect for the environment or nature which saddened me the museumsummers are in a poor state which is also very sad there is little or no information and much of it is in bad shape everything costs not a little bit more for tourists but 400 or 500 % more which is not great if you are a backpacker with limited funds also you are discouraged in making friends with egyptians in the way that if you go out with one they can be arrested for not having a tourism guide certificate we were out with a friend from cairo and he was almost a rested for being a &quot;fake guide&quot; this angered me enormously
98 1456254710
crazy town full of life history and sights   amazing food in places  people very friendly    very diverse place
28 1456255282
cairo is very safe  in the us there is a misconception that egypt is a dangerous place  i like to walk and have walked all over the city i have never experienced or seen anything to cause me any concern and this is my second visit in two years   americans come to this beautiful country
49 1456292931
i loved cairo and the people and the enrich of the fun in the air and the good people and the weather was amazing and also i wish i could stay longer
7 1456341597
pyramids  shpinx  egyptian museum  old cairo  the food is great  is very crouded specially during the rush hours
30 1456376134
no comment
53 1456395367
dirty city crazy traffic no fun to walk around loud people everyone wants money from you
10 1462278552
beautiful place
33 1462281564
cairo is an amazing place to visit if you like the culture and history it has to offer the atmosphere is unbelievable during the day and night amazing shopping markets and not to mention the unforgettable pyramids which never get old   places to visit - mina house for perfect views and food
17 1462290554
bazaar museum  giza  opera house concerts
4 1462301455
i always enjoy cairo  an amazing city by all means it is an unforgettable experience and i want to visit it more often
19 1462323313
getting around cairo was very easy because of transportation  i wouldn&#39;t advice hiring a car though because only the locals understand the driving in cairo
21 1462326199
the food  the people
99 1462350932
depending on your interests i would say everyone should visit egypt once in their lives i think there are many hidden gems the pyramids and the markets are a must 2 full days packed with activities in cairo should do it
53 1462355532
i like to sites around cairo it would be great to have a friendlier face especially when you spend so much money
57 1462363276
cairo museum is essential but stuck in the last century a shame  make time for the other sites most of them are good
9 1462420524
beautiful history but very dirty and crowded country
28 1462440684
a city full of great and ancient history for people who love the kind of staff otherwise cairo wouldn&#39;t be good option for people who looking for relaxing tripe
49 1462446128
cairo is a wonderful city
22 1462446372
city of beggars touts and scammers
33 1462456796
food was awful we paid a lot of money for standard rooms for our driver and a suit for us and business lounge and we didn&#39;t eat as the food was so bad and the hotel did nothing we had unacceptable responses from staff and they were more interested in the food africa party in the hotel and ignored the rest of guests
78 1462468940
nice food friendly people
2 1458560328
brilliant place lovely people
49 1458571934
craziest traffic i have ever seen! there was no way i would ever try to drive there  however the taxi services are cheap and reliable we used uber most of the time - and it still worked out cheap! if you are not fluent in arabic - get a translator app!  the people were very friendly and kind making the experience unforgettable
13 1458574886
see above
81 1458632921
crowded people are not that friendly as promoted
93 1458644979
the pyramids and cairo museum are well worth a visit but don&#39;t stay too long there are many amazing places in egypt
67 1458715026
just here on business would love to have more time to visit
81 1458724443
cairo is filled with magnificent historical sites and ancient landmarks unfortunately most of them havent been properly preserved it&#39;s a pity for a country that has a wealth of history culture and charm to share
47 1458737455
apart from the historical and museums not a great place for solo travellers
40 1458749167
cairo is a lively city its people are warm and welcoming it is a city full of hidden treasures and untold stories i have been twice only to cairo but each time on my way to the airport i hoped to come back again and discover more about her
66 1458815818
amazing huge city
17 1458849662
city wity great potential but very neglected by government
56 1458851918
it is amazing! i recommend everybody to see egypt for the artifacts that it has and ancient history especially luxor and of course aswan but luxor is amazing! cairo is fill of the history
65 1458860029
from pyramids to islamic and coptic partsthis city is a heaven for history buffs
29 1457265941
simple - from my point view i wouldn&#39;t advise going here until the country is more stable     the hotel -     i have traveled a lot in the middle east for work purposes and generally always stay at a meridien hotel as you are almost always guaranteed a certain standard at a reasonable price - the meridien hotel - the pyramids is not a typical meridien hotel do not stay here     westerns staying here are treated like walking wallets (you get a price for something and it is immediately bumped upwards and within minutes if people think you might have money) in short you are sized up the minute you walk in the door the staff at the hotel asked outright for tips and women are constantly bothered by an all male staff i include myself in this and also saw other women experiencing the same an abaya which should usually deter male attention did not discourage! the staff i felt had suppressed aggressive and servile attitudes their resentment for the west and westerners is very clear even when they think is veiled by their wide smiles - it was palpable     we visited the pyramids -     i would advise avoiding the horses and the carriage rides they are unsafe and the animals are treated appallingly very sad if you like animals (i understand it is a very western view but i happen to love animals so i am naturally defensive when it comes to the issues of treating animals poorly!)    we were told we had to have a guide once inside the area (i am not sure that this is true)!  as like the hotel westerners are treated like walking wallets and you should expect to be fleeced for everything you have guides there will offer you a price and will try to double the price by the end of your trip don&#39;t assume once you have agreed a price that this will actually be the price at the end     once you get a price your guide will conduct a carefully practised routine of relieving you of your money as you go around the pyramids be warned! it helps if you are clear about what you want to see and what you don&#39;t want to see and i would suggest tying them into a time by saying that you need to finish by a certain time as you have something else to do they will also try to take to you the shops afterwards to buy lotus perfume and papyrus papers so make sure to make clear you do not want to do this if you do not    guides will also demand a tip off you at the end if you don&#39;t offer one even if the price has gone up for the tour our guide tried to double the price by the end and still demanded a tip also expect it to feel uncomfortable and defensive when you say &#39;no&#39; to them they don&#39;t take it so well     having said that the pyramids were worth seeing but you can absolutely do it on foot (i would avoid going if the weather is too hot or else go there early in the morning or at night when you can do it one foot) i do wonder if you are allowed to do it without a guide (they tell you the government insists that you have a guide but i do not know if this is an actual law) it would make it a much more pleasant experience if you were allowed to go without a guide it was a pity about the guide as he became the centre of the experience instead of the pyramids it was hard to enjoy the experience    i personally will never come stay in egypt again (with the exception to maybe get an connecting flight) it seemed to me that at the moment it is a country divided in two halves i am sure that many egyptians would be genuinely disappointed about what i have written but i can also imagine that many would delight in the fact that my stay was so uncomfortable time will tell about which direction this country will go in
67 1457274725
khan el khalili mosques museum traffic is awful taxis are very hard to engage haggling never ends a good example of the orient
9 1457279144
nile river cruise is very good
98 1457292181
to much to say everyone must visit egypt
13 1457326087
there is always something to do and amazing things to see in cairo you could never have enough of cairo
78 1457433463
nice  city
30 1457441827
cairo is very nice arab big city  it has kind people and value historic places  high culture even if it is poor city
34 1457444983
the light show at the pyramids is worth seeing and hearing they take you back where you feel like a pharaoh sitting on your front porch watching the show
72 1457451903
egypt is amazing value at the moment
58 1457454666
amazing
2 1466724456
too much bargain for businesses people demand tip for everything
8 1466751922
not well maintained too dusty the pyramids are not well preserved    i was very enthusiastic and had lot of expectations  hear d lot of things about egypt but
65 1466852137
scam-city  i&#39;m a budget traveler and so i tried to do all the things on my own and it was just so exhausting to fight off every new trick or scam that i felt like i could not enjoy anything much less have conversations with egyptians about their city or country i feel like people just saw me as a walking atm machine for the first time in my life i wished that i had just booked an all inclusive package tour of course there are many wonderful egyptians but they just won&#39;t be the ones trying to talk to you
50 1466882268
cairo is great i am moving there in a week
92 1466903930
did not get a good bite of it
83 1466934510
excellent food rich cultural asset very deeply rooted people and very kind
86 1466934685
the people and their behaviour
87 1466937395
great city
48 1456411982
use taxis for transport cairo is not an attractive or pleasant city  just come to see the pyramids
5 1456474163
low points: traffic and bad driving!! also be ware of tour guides trying to hassle you with buying unnecessary things - in a quite an aggressive way the tourism industry is currently suffering a hit from political and security issues so a lot of people trying to make ends meet by making money of tourists unfortunately  high points: the pyramids and the sphinx the egyptian national museum nile river and middle eastern food! marriott hotel in zamalek has an amazing refurbished castle architecture good to visit restaurants: jw&#39;s steakhouse in marriott hotel zamalek - probation era chicago-esque feeling with great steaks
83 1456487374
hate the cityam never going back again! dirty and dingy
23 1456616095
i&#39;ve been living in egypt for the past 4 years and as for me cairo is beyond comparison a dynamic interesting city more civilized and progressive than vast majority of other places in egypt adorable al azhar park interesting citadel and other museums although the cairo tower was not impressive (after ostankino tower in moscow hehe) ate in many places there are great cafes and restaurant with syrian food lebanese food egyptian food etc enjoyed cairo subway when was before my pregnancy i am sportive and fit i am curious and brave so i find cairo suitable for me to stay short-middle term   this city has well-known disadvantages: &quot;zahma&quot; (crowds) traffic jams pollution and smog rude kids and teenagers curious about foreigners people i would avoid going to cairo zoo alone especially if you are female - groups of scholars there could irritate you beyond the limits
11 1456646382
lovely and kind people delicious egyptian food  the pyramid experience was awesome especially with horse riding   i loved city star mall as it has many stories and lost of restaurant and stores
80 1456659476
some parts of cairo are extremely beautiful but the traffic is the horrible one that i have ever seen so if you have enough patience to the trafiic and to the crazy drivers i recommend you to visit this city
45 1456671194
down town cairo is cleaner than before and i really enjoyed visiting two huge malls ( al arab and cairo festival)
84 1456671545
spent a weekend in cairo i really enjoyed the souq and the culture but disliked the traffic pollution and lack of cleanliness
10 1456702511
easy to get around cairo taxis galore  average taxi price for major sites 70 egyptian they will always ask for more but be clear that you will only pay 70 and they will not let you go because they make pennies on local users and tourists are the real income   one of the best tour guides i found by mistake mr muhammad swefy around 34 years of age fluent in english and a historian    after site seeing he is also great to take you to the a smashing shopping+education experience he will take you personally to the craftsmanship workshops where goods can be brought much cheaper and you get to see how much skill is required to produce such amazing souvenirs for us tourists contact him before travel or once you are there on:     muhammadswefy@gmailcom or on        0106 2676 212     reason why i took all these details is because i would recommend him to my family and friends 100%
20 1456724738
citadel very old   khan al khalil amazing for shopping  cairo tower we waited too long in queue  sphinx look better of photos  the pyramids is the best  we enjoyed the nile cruise and belly dancer
47 1456749344
we had 2 full days and one evening for cairo we traveled with kuwait airlines and w reached there within 12pm afterwards we were transferred to giza cairo has big traffic so it took a while to reach to our hotel at giza  normally most of the attraction is closed by 4-430pm so we could not enter any main attractions so we took a trip to go to azhar park one the way we could see cairo citadel then khan al khalili  and muizz street after doing that trip we came back and finally had  dinner  the next day we started our tour with our beautiful guide laila the trip consists of the great pyramid  saqqara- the first pyramid memphis shopping at giza then we saw sound and light show at giza and then finally transferred to city center hotel     the last day of cairo and egypt we visited the egypt museum  then saint sergius and bacchus church then coptic cairo tour and finally to the hotel     so 2 full days are good for cairo city
79 1456801069
the egyptian museum is over-hyped and can be avoided if you are not on one of those museum types cairo is a city where you enjoy in the chaos and the energy sheeshas in the middle of the road and endless cups of shai make sure you visit khan el khalili for the craziness  and coptic cairo is worth a visit too especially if you go via the metro for an added experience of feeling like a local
25 1456834405
beautiful dynamic city withe a great civilization
10 1456837550
museum;  pyramids;  cairo tower;   dining at nile cruise; and   many morecarry a gps
86 1456839243
go to a small local cafe smoke shisha mingle learn!
85 1461839117
cairo is most attractive  city
97 1461839588
القاهرة جميلة وطبعا سرايا على النيل   وفي موول جميل وكبير اسه سيتي ستار انصح بزيارتهم لوجود مطاعم مصرية جميلة   ذهبت الى المتحف المصري وانصح الجميع بزيارتهم  الناس جدا ودودين في كل مكان
37 1461842257
ancint places are so great the pyramids are majestic and the museume was spectacular and highly educational especialy with a guide
34 1461849523
hotel
24 1461863652
the giza pyramids are amazing ignore all the scam tours and donkey rides hike up and enjoy a day wandering around tip:lazy arabs will stop harassing you at hottest time of day and layabout in shade midafternoonish museum is also great better to do it all in a day and leave cairo its a real sh!+hole and everything else in and around cairo is terrible compared to giza pyramids and museum spend your time in luxor aswan and abu cimbal nile cruise from luxor to aswan is awesome
90 1461916123
cairo is my lovely place to visit malls ancient egypt islamic egypt restaurants    people are having good hospitality
89 1461993954
super friendly people and so much history
13 1462088878
room with good nile view no activities for children aged 6 to 12 years
95 1462095279
pyramid was amazing place of which should not miss to explore
0 1462125696
cairo is a good place to visit the people there are very friendly and the food is just delicious nile river night cruise and  the pyramids are a joy to watch just wish i stayed longer
62 1462126238
pyramids will take your breath away
4 1462137882
lovely city
72 1462164664
beautiful city people are friendly this place is a must visit for all
75 1462166627
it is really a historical city
0 1462172503
i love cairo everything is great  most is wonderful people  second best delicious food  prices are so attrative
66 1462181508
as a solo traveller language barrier is really a problem so you need to be careful and vigilant in very countries especially in cairo (arabic language) 🙃
67 1462184724
city centre
92 1457933238
the citadel is not worth the hassle it is only good if you want to buy souvenirs the prices there are very competitive
6 1457949018
the people are hospitable (except a few brokers on the streets who &quot;own&quot; stores all over and bother travelers asking them to buy &quot;flowers&quot; something needs to be done against this menace since you don&#39;t know who is genuine and who is not    the visit to the pyramids was excellent
61 1458013834
korba is the besy place   taxi drivers are the worest thing
36 1458032563
friendly and funny people there are a lot of museums it&#39;s also cheap city
85 1458035881
all land marks were brilliant however the roads in central cairo are of poor quality
45 1458037847
fantastic place to visit
63 1458043544
crowded and noisy
89 1458046216
i loved the hotel security all over the hotel   everything was perfect  i didn&#39;t get good room view
22 1458068057
tourist theives from taxis to tourist sites very upsetting even though i am a muslim i felt these people were worser than animals!!   do not visit!!
0 1458071963
the food and the restaurants we ate at
81 1458073202
you always have another place to explorevery safe
35 1458118989
very nice place night life is amazing
93 1468282290
went only to see pyramids sphinx and museum (always wanted to go) but there is so much more to see and do there    cairo and giza are amazing just to stroll around and take it all in definitely recommend walking around the backstreets eating local food and visiting coffee shops etc     actually seeing the pyramids is incredible get there early to buy a ticket if you want to go inside costs 200le and you will be glad you did  you hear of the pizza hut etc next to them and all the rubbish which ruins the pyramid experience - it&#39;s absolute nonsense once beyond the gates it&#39;s as though you&#39;re in the middle of nowhere (other than some annoying &#39;guides&#39; trying to lure you onto camels)    cairo itself is manic don&#39;t expect a relaxing tranquil holiday if staying in the centre!  found that maybe 1&#47;10 locals that speak to you will genuinely just want to chat the rest you will find trying to lure you into their gift shop with stories of their friend from yorkshire     we used taxi if we didn&#39;t walk as it&#39;s so cheap for 100le could go from hotel near pyramids to cairo wait for us for 5 hours and go back again (wouldn&#39;t walk it!)  between giza and airport paid 120 and it&#39;s about a 45 minute drive could probably get it cheaper but there&#39;s no point and everyone&#39;s happy at that price    recommend cairo tower to see the sheer size of the place and the pyramids on the horizon costs 150 i think well worth it!    museum is definitely worth a visit i got a guide for a few hours as i hardly knew what anything was that i was looking at     when people say the city never sleeps it&#39;s true not sure what they&#39;re doing all night but there are people about shops open 24&#47;7 and lads playing footy in the streets at 4am  highly recommend a visit
7 1468320078
laughing with egypt peoplethey always smile
28 1468342300
use uber for local travel keep egp in all denominations generally ppl are friendly and good and helping
15 1468345764
lots to see  lots to do a great vibrant city with a mix of old and new things to do
68 1460917362
rivet nile  pyramids  mosques  pharonic museum
58 1460963843
obviously historical sites like the pyramids of gizathe museums lots of mall and shopping for the wifea few good places for the kids to enjoy food is great if you go to the right places use foursquare as your guide must try zoooba and baladina they have multiple branches
79 1460973118
noisy and dirty many many people in pyramids trying to ask for horse riding photo or whatever making the visit a sad memory the best solution do not talk to anybody! do not buy anything in pyramids do not pay for a horse &#47; camel ride! on the other hand we feel safe anywhere any time of the day&#47;night  all over cairo with 26 millions moving around we did not see even one traffic light!
1 1460976384
great shopping great museums great for families
44 1461053971
i felt so at home in cairo such a wonderful place and wonderful people
13 1461080565
safe fun interesting lovely people
21 1461085925
live it simple
18 1461099383
the pyramids and sphinx are fantastic egyptian museum is fantastic really enjoyed our dinner at casino des pigeons stayed at le meridien pyramids nice and comfortable a week wasnt enough traffic around caironot so good dont pay the full asking pricefor anything
39 1461114752
there&#39;re plenty of things to do in cairo you can easily find activities and places that match your interests from shopping malls cafes &amp; restaurants sight seeing nightlife &amp; parties to a nile cruse or a tour in the old cairo maybe  the city however is so crowded and you can get stuck in traffic for hours during rush hours i don&#39;t not recommend using public transportation like buses or the subway if you are not familiar with the city or if you care much about have a personal space! a taxi is usually a better choice but make sure he&#39;s using the standard fare or you will probably get scammed  avoid dealing with beggars and always make sure to watch your personal belongings (like bags wallets phones etc) especially in crowded places  stay safe and have fun!
73 1461142760
anyone approaching you is seeking your money (sorry but no exceptions i&#39;m afraid) and dealing with it is exhausting try to use the metro (subway) whenever is possible and have spare change to pay exact amounts or you will lose lots of money    other than that try to focus on what egypt was a long time ago there is nothing like a pharaoh&#39;s headpiece in the rest of the world and so are the egyptian temples gods and hieroglyphics    if you are looking for authentic stuff avoid &quot;made-for-tourists&quot; street markets such as khal el khalili where most things are made in china and and search for serious shops like verois valley    last tip: do never let annoying money seekers to ruin what can be an unforgettable trip!
94 1458212140
the nile view
35 1458213355
people are very nice  but you have to have a lot of cash  tio every where you go  they called bakshiesh  even the limousine taxi from cairo international airport ask me for tip as his son in the hospital and need operation   the travel from airport to the fairmont hotel less than 5 minutes i did pay 200 le  so far so bad
25 1458215376
fabulous!!
60 1458226890
friendly people
66 1458235876
a fantastic cosmopolitan city
0 1458249721
nice people   crowded city   pollution   good cheep popular food
31 1458290829
cairo is a special experience for travellers who are searching for a unique taste
37 1458304889
good city combines modern with past  great historical places to visit  general public are good nice hotels cafes shops restaurants
29 1458314701
it is easy to go anywhere in cairo  very safe places  lovely people simple cheap resturants  u will enjoy  try to visit it
40 1458318838
3 day stay in cairo&#47;giza visited all the top sites including the pyramids 3 times and some of the not so top sites plenty of time relaxed stay ate like a local and thoroughly enjoyed my time tried koushry in a local restaurant that only serves this one dish and it was great mint tea in roadside coffee shops a game of dominoes and sat and watched the world go by  try to avoid the traffic but if you can&#39;t it&#39;s a welcome break from the sightseeing to rest your legs cairo museum is vast and really needs a full day or a couple of half days at least if you can avoid the hassle at the pyramids it is amazing and the sound and light show was great as it was quieter than the day time loved the citadel and coptic cairo - no touts or hassle and a leisurely stroll around i chose to get a taxi everywhere which i got from the hotel but felt safer like this as a solo female traveller he was with me full days for the entire trip and was really helpful friendly and informative all the egyptians i met were lovely and couldn&#39;t have been nicer or more helpful
37 1458368720
i stayed for two days making it just right i think seen the pyramids first day and second day went to museum book accommodation on the nile if you could early to avoid getting a dump
34 1458372170
great cairo is beaten by the worst ruling regime ever
23 1458384426
giza
29 1458384509
lot of traffic  noisy     nice tourist  area but required more to be clean
63 1458396208
i didn&#39;t get time to site seeing
7 1458474207
great place and wonderfull
58 1458484560
avoid old nice historical places v poor quality
35 1458486097
great food experience and the people are very helpful     alot of people are asking for money
95 1458493306
get a guide and driver it is worth every penny the giza pyramids and a mini cruise down the nile     i can&#39;t wait to visit the new museum maerdy is a great place to eat there&#39;s plenty of choice and it&#39;s a pleasant atmosphere
33 1458507101
it&#39;s a great city people are friendly lots to see best way to get around is to use uber or careem both are apps to call taxis i used careem you will find many activities but cairo takes time to figure out
28 1458554861
almoez  street is great ancient cairo  crowded traffic
96 1466404119
- lovely people  - cairo is full of interesting placesfull of history and culture
63 1466413128
to have seen the pyramids and the sphinx up close as well as the citadel and egyptian museum was worth the trip the negatives sadly far outweigh the positives  from the ongoing simply irritating shopkeepers camel and donkey owners to the taxi drivers to the bizarre filth everywhere its no wonder people are not returning and when they do they don&#39;t venture outside of the hotels
62 1466413831
old fantastic city  with historical spirit and very nice modern facilites   all family enjoyed on different preferences  everything you can found very chip    i visited  huseein mosque  old cairo at same time   i spend my night at portocairo mall  mall of arabia   very nice trip with very cheap cost    only thing  it is crowded   i advice every one to go and visit such fantastic  old city
76 1466486130
cairo  is just ugly and miserable
30 1466501704
i am a resident of cairo and the city is much safer to visit than media is leading people to believe don&#39;t be afraid to vist
34 1466532736
visit cairo if interested in historical places
81 1465875680
cairo is a lovely city full of life however the cleanses of the city is not great and the suppressing feeling was overpowering
1 1467810924
i loved everything
97 1467880958
the cairo museum and of course the giza pyramids the staff at the hotel are always helpful to give information for your stay
68 1467893053
wooooow
31 1467897557
khan el khalil   i ate at najeeb mahfouz
80 1467902198
giza pyramids are amazing but there is also the bent pyramid of dachsur stunning surroundings and very quiet well worth the journey out there
10 1463129848
it&#39;s different but the people is friendly my advice don&#39;t go by yourself anywhere always ask tourism company to go with you in that case you can enjoy an amazing trip
14 1463133547
cairo is good because it is near gizathe only real reason to visit this city are the pyramidseverything else is secondary beyond this the cairo museum is a must seetutankhamen! as a single womanbeware and take advice from your accommodation or make sure you go on group tours cairo is a must see but does not represent the beauty of egypt it is a huge bustling struggling metropolis where many people are fighting to earn an income to support their families as such travellers need to be aware and remember what has happened here in recent times but do not miss the pyramidstoo remarkable!
92 1463147643
cairo is amazing!! we organised a private tour which i would highly recommend we were taken to all the best spots and looked after really well we weren&#39;t bothered by people and felt very safe
0 1463201074
cairo people are loving plus friendlythere are many historical places to visit including museaum and pyramids  food is very cheap or you can say overall cairo a very cheap city for spending holidays
2 1463209525
very nice historical city and lovely and friendly people
60 1463209603
cairo is a great city to visit and lear about egyptian culture and history people are nice and helpful we felt safe and at easy of course one cannot expect the order and style we are used in europe and north america the pace is hectic but fun if one knows how to appreciate it
86 1463216865
the pyramids  and sphinx  i ate coshari  which i like
88 1463221941
amazing city cairo has lots of attractions ancient monuments mosques and many others places to visit;    1- pyramids  2- nile river cruize  3- pharaonic village  4- dolphin show
69 1463240941
crazy place with so much to see
39 1463250865
taxi drivers are the worst i have ever seen( i have travelled the world) they will rip you off overcharge or bring you to the wrong location     small vendors in kan el ka lili same as taxi drivers and extremely persistent same with the locals at pyramids ( annoying and will rip you off     i do not recommend cairo to a first time traveller  they will eat you alive   as a seasoned traveller to africa i was used to it and able to deal with the aggressiveness and them trying to rip me off
15 1463291400
cairo is a fantastic city to visit it is a bustling historical place that has so much to offer obviously you must visit the pyramids and surrounding historical sites    the people were incredibly thankful to us for visiting their country as tourism plays a big role in their lives     we thoroughly recommend a visit to cairo and egypt we felt completely safe the whole time we were there
67 1463292157
our trip became excellent because of the tourist guide that the concierge at kempinski arranged for us sara eltahan spoke excellent english and has a masters in tourism with encyclopaedic knowledge of the history and culture of ancient egypt she can be contacted at sara_latif@yahoocom or cell 01000755245 and 01204881811  we visited the museums mosques and the bazaar the pyramids and the sound and light show were fantastic the cruise on the nile was also good however all of this came to life because of our guide
57 1463304681
overall cairo is very cheapmany historical places for visitingcairo people are nice
62 1463305341
busy city
9 1463338874
the culture and people
43 1463344332
great city
80 1463356554
such fantastic and magnificent place on earth
44 1463383787
zoba resturant is a must love it  abo alseed in zamalek  sofe in zamalek  diwan bookstore  atlantis restaurant   hebta the movie   very easy to get around and ober is the best  taking the boat almarkeb in the nile
78 1463386000
cairo is amazing but allow plenty of time when moving around as the traffic jams can be horrendous!!
3 1463387782
of course pyramids and museum
14 1463390219
taxi drivers are nightmare they charge travelers any price they think if you don&#39;t know anyone in the city then you will get the worse experience ever cairo museum charged 75 for non arab as entry fee where arabs pay only 10 inside the museum mummy place they charge 100 for non arabs but local enters much cheaper price
49 1463397308
locals very friendly and helpful
9 1463424909
beware every egyptian from your first step at your port of entry is either a professional rogue conman or a fraud out to fleece you if a couple a bucks    be extra careful i warn you from multiple previous experiences
54 1463432735
there is much to see and do in cairo and surrounds the popular attractions are frequented by helpful locals know the value of their currency and carry small bills  egp 10 is half days wage ( about us $ 125 )  travel times are slow with more than one hour to the city being the norm  employ a car and driver that can take you to local markets and lesser known attractions to get a better feel for the town and much cheaper shopping  i recommend a trip in the country  for example ismailia on the suez canal a 8 lane expressway with much less traffic than cairo  negotiate with drivers for a better deal
99 1463438808
i road a camel through the desert and saw the view of the pyramids up close i ate local food some of which was provided by the hotel while others i got from around cairo
3 1466248185
the river nile specially during evening to night is marvellous nile city restaurants are good   and the view is cool
3 1466251756
vibrant city for good night life amazing nile river journey makes you more happyvalue for money spend on various tourist sight seeing areasmore shopping with less cost
48 1466258502
there are many good people but you need to get out of touristic area to feel egypt do not tourist facing people shape an impression of cairo
17 1466327283
crowded people don&#39;t care about tourists the main aim is to take what is the tourist booklet
63 1466350470
pyramids and a lot of place and trip in ship in nile river but cairo so crowded and there are a lot of delicious foods
34 1466351868
friendly people nice food and lovely weather also very budget friendly
67 1466358008
cairo is a great city to visit and enjoy traffic and travel is not bad  improved a lot from the past egyptians are very friendly
33 1450813888
the main problem was every person offering transfers wants to rip you off at the airport
77 1450825115
u can go spend good time with lovely honest people  safe city u can fined cheap good food picnic in nile river also u can visit museum pyramids   u can use taxi but check the counter to avoid to pay a lot thanx
69 1450868378
safe and people are friendly don&#39;t always believe the media
75 1450869051

73 1450952836
fel fela best place to eat in tahrir sqbeware of nicely friendly talking people coz their intention is business at t end pointso get t info and say shukranmeans thank uif u r visiting pyramidsbest place to stay is meriden pyramidsexcellent stayfriendly staffgd fd&amp;poolfel fela restaurant is 2mts walkgd fd&amp;not tht expnsive
30 1450953950
eat in t hotel or fel fela which is 2mtsto see pyramidsif u get a deal from hotel fr 400 egypt pound its gdwe took a private tripone mrsyed 43years of service just outside hotel entrancetook frm hotelclean cartook to the tourist guidance office near pyramidfix t deal fr 850egp including tktsenglish guide(horse cart driver)photo grapher(horse cart driver)baby sitter(horse cart driver)horse cart driver is clean neat and has a british accenthe took amazing pic of us coz he knows frm where and how to take memorable piche helped t 2yr old kid to carry down into t narrow way inside t pyramidwas nice trip in pyramid
82 1450975183
lot of polution
86 1450997542
local food is amazing  avoid rush hours on ring roads
28 1451096396
never traveled much this time was here for a wedding and we ate out side because it wa more expensive in the hotel we had friend to take us around i would avoid the taxi drivers they try to rip people off
86 1451126271
cairo is overcrowded city traffic is horrendous which means you cannot get around easily too much honking gets on your nerves but managed to do what i wanted to do: seeing pyramid
37 1451127159
history  locations and food
16 1451134373
great shopping in cairo the magic of pyramid and the charming city
71 1451148907
easy to get around and it&#39;s ok to me
21 1451158836
i can&#39;t say cairo is a tourist friendly city people are so unwellcoming &amp; tout taxi drivers most of them are impossible they somke when they drive &amp; play radio on high volume food is good though it&#39;s very good if have some one with u who speaks good arabic
52 1451212817
nice and friendly
65 1451216167
great time and place  elqalaa   elzhar park   el fayoum el rayan
58 1451225279
شعب شحاد من رجل الأمن لحتى الولد الصغير الي ببيع بالشارع كله بشحد منك
88 1451225578
sightseeings shopping friendly people culture historical places
72 1451230515
we like city museum and pyramids  people were very helpful  lots of see  but need more indian restaurant  yes sufi and belly dance were very nice too
67 1451235073
pyramids are good buy the place is not well maintained beware of the camel or horse ride scams they can charge anything if you don&#39;t know the local language they will ask usd    sound show at pyramids was very good    beware of the sales agents at the historic sites at giza pyramids the camel ride man takes you to a lotus perfume factory the salesman can charge you anything for the perfume clear scam you need to negotiate a lot if at all you decide to buy    otherwise nice people developing country great history
61 1451256909
after arriving from addis ababa  ethiopia cairo feel like being in heaven with people so friendly safe to walk around at night not having to worry about gangs of pick pocket all the time food fanatic a restaurant called fafeal good to experience traditional meals for one night local takeway cheap and tasty compare to a lot of africa food which is blend  egyptian museum was great could spent whole day  for transport their a good subway and taxi are cheap
0 1451290408
pyramids even though you are constantly harrassed by someone begging for money trying to sell anything and in any way don&#39;t trust anyone even the &quot;staff&quot; wearing a badge is there purely for money you can avoid the sign &quot;do no climb the pyramids&quot; just with 5$   the egyptian museum is left by itself since 1850 great collection though   better eating in international hotel chains and move always by taxi impossible walking from a place to another due to constant harresement and request of money form everyone the level of pollution is incredibly high there are no public transports
50 1451317236
the old part of cairo   the egyption people very polit and kind
53 1451349702
the egyptian tourist industry is at its very low right now  everywhere you go you will find cheats and liars  please be aware     it is such a shame  the country is full of nice great people but is spoilt by people who constantly want to take advantage of you    beautiful ancient egyptian history simply spoilt by the constant pestering by local guides temple gate keepers etc  they do see you as a walking dollar sign  it would be okay if when you decline their offer they would back off politely the problem is that we encountered with many angry responses  it was even scary
2 1451375346
it was a business trip but i made a quick trip to pyramids
4 1451414276
pyramids and down town in addition to islamic places such as hussien khan elkalili and elmoez street and for the modern egypt city stars mall and cairo festival are great
15 1451463101
lots of historical sights food is great and people are really friendly
65 1451465533
staying in downtown cairo means you are in middle of everything great country with nice people pyramids cairo museum cairo castle  old coptic cairo and so many other wonderful places crowded roads
78 1451567456
cairo   sharm al shekh   sea food resturant   italian resturant
55 1451573464
every thing in cairo is recommended to visit
50 1451625508
basically i am on business trip so cannot really answer the question
67 1451637479
best holiday
67 1451651772
weather mosques and sightseeing
97 1451713176
ancient places were a must to visit  however  not well maintained and need to accept credit cards as a tourist destination  ate at falafel azhar park  again  needs to accept credit cards and not to insist on tips the waiter at falafel restaurant came running out to me telling me the tip was not enough ! nonsence attitude     we had a private car but cairo is chaotic and bad traffic we were only saved by a lovely tour guide and driver     airport and its services was the greatest setback  the bus transfer inter terminal made two stops to change bus and eventually dropped us at the carpark wherever had to lug luggage across to the airport for a good 100 meters then there was only one access to the departure as the other access was closed again juggling with the crowd  while we were waiting to check in ( we were on business class ) the security guard rudely chased us away from sitting as he wanted to relocate the seats !   egypt should improve on its services if they want to regain its tourist industry !
3 1451729304
nice city
10 1451744507
it&#39;s a buzzing city that never sleeps friendly people interesting experience not recommended for the fussy traveler
82 1451819102
it is a very exciting cosmopolitan city with lots of hospitality by its people there is so much to see especially the historical sites and visit if you have time
6 1451821210
the main attractions are the pyramids but don&#39;t miss the egyptian museum and the cairo citadel be prepared to be stuck in traffic a lot of time
96 1451821352
cairo is a chaotic and crowded city it has a metro system but not very well connected and doesnt have enough of instructions taxi is cheap but you should bargain for a price before taking it try to use white taxis with black stripes on the doors for pyramids i suggest you to arrange a guide which was 60$ per person for a group of 6 people in our case including pick up from the hotel guidance camel drive around pyramids and drop off to the hotel otherwise its quite complicated to handle by yourself i suggest a visit to egyptian museum which is a must! you need at least 3 hrs to visit and there are guides you can easily hire within the yard of the museum unfortunately instructions in the museum for items are quite poor it might worth to have a guide youll see tutankhamuns mask which is amazing! also it worths to pay extra for mummies room! once in a life experience keep in mind the museum closes at 4pm citadel and old cairo would be the oter attractions not to miss also khan al halil market is quite a nice spot for shopping and visiting keep in mind egyptians have their lunch at 5-6pm and their dinner is around 11pm you might need to adjust yourself accordingly! and shisha is an inevitable part of their life every night after dinner they all have shisha you can try some spots around nile with a perfect view for cairo tower i cannot say its a must cause cairo has airpollution problem and its usually hazy you might not see anything for this i suggest dos canas a spanish restaurant around american embassy and egyptian tower on the rooftop of cairo capital club with a stunning view food is also so nice! but its quite pricy for any of your plans especially on the way to airport please mind the traffic the traffic might stop and not move sometimes for even one hour if you dont want to risk it you can take the metro to the airport from downtown but youll need to change lines for a few times line 3 from attaba takes you very close to the airport (unfortunately not to the airport itself) and the stop will be called kolyet el banat and from there you can easily take a taxi which should take 10-12 mins other than that cairo is a different experience try to enjoy at its best!
6 1451832235
it was great!
77 1451838245
na
25 1451844874
the people in cairo were some of the most generous and helpful amongst all of the places i have visited so far it was sad to see such a lack of tourists when the monuments like the pyramids are such incredible sights to see one of the many highlights of being there was when me and my friend decided to do a day-tour of the pyramids and museums and at the end our guide decided to treat us to a complementary sunset boat ride along the nilesimply because &quot;we were his first customers of the week and he wanted to show his appreciation&quot; and it was amazing cairo is definitely rougher than the average tourist destination in terms of their driving and their communication but once you are able to get used to the culture it is a very beautiful place
27 1451846801
n&#47;a
87 1451861311
lively and buzzing
58 1451875592
the pyramid &amp; sphinx going down to the pyramid chambers going to el khalili bazaar and have egyptian coffee or tea at el fishawi go to egyptian museum
32 1464247873
amazing city with amazing people
60 1464253374
the pyramids sphinx museum citadel old walled cairo city and coptic cairo were great the nile cruise was too slow going and i suggest to those who like a fast pace to rather arrange their own trips to visit all the archeological sites worthy of seeing instead of a nile cruise
54 1464263595
boat trip at nile river   bring with you all stuff needed for the cruise  visit the pyramids  we enjoyed coffee shops facing nile river  the events done at the hotel were more than enough
35 1464269726
warm-hearted people crazy city with immense historical culture  traffic is bad though that is true of many cities sadly
2 1464338842
certain areas are good  there is deterioration in the cleaniness of the city from i last time there 20years ago
89 1464341900
everything
11 1464348843
great place
78 1464428212
cairo has some many attractive areas!
70 1464433285
we deliberately avoided cairo city and stayed at giza instead
26 1464436563
cairo is marvel cityit is life for mei supose every oe to visitit has so many taste of life  luxurymiddle relaxsight seennile viewmuseumgood weatheri like to visit cairo more and moreegypt is the most attractive country for holidayswith any value of money  u can live and spend yr holiday
27 1464514484
the area around tahrir is very lively - especially at night also i really like the hossein area
42 1464514504
museum phoraoh village khan al khalili mo&#39;az street opera house
48 1464518592
the traffic is hard
93 1464524077
cairo is a great city but with an awful traffic people are nice and hospitable food is good and tasty and who wants a good shisha for sure cairo is the best place to be there are a lot of places were you can relax and enjoy the local culture  if you are in need of a certain sophistication quiet cities with relaxed traffic and all of the benefits of the modern society and technology cairo will not be your best choice
93 1451923015
the museum is exceptionally good  the pyramids are worth all the rest of the bad stuff as they are spectacular    i recommend that you take an arranged tour so you always have a guide to avoid the hassle of all the people who constantly badger you  they really ruin the trip by tricking you into things that you end up having to pay for
42 1451948650
cairo was full of good places to visit  needed tk extend my stay much more in order to check kore places
58 1451982504
giza pyramid luxor aswan is a must do places for egypt and in fact the world! :-)    we had such a good time in cairo especially giza pyramid was amazing thing to see!
38 1451993118
the visits to historical sites were quite a revelation   the experience of a tour to the pyramids of giza on a horse cart a visit to the first  pyramids built in egypt as well as the pyramids in sakkara were an eye opener   we experience how one of the dancers on the nile boat could count in more than 20 different languages  the egyptian museum displays antique products mummies and coffins of thousands of years old  the marketplace (khan al khalili) needs all your time to choose amongst the different novelties  old cairo is a place where we could reflect on our spirituality and find some perspective    something we couldn&#39;t get used to was the hectic traffic in cairo don&#39;t think about hiring a car rather make use of an experience driver who knows the roads of cairo
49 1452003340
while cairo is a big polluted busy metropolis the views over the nile are tremendous and the giza pyramids are definitely a site worth visiting the egyptian museum has some incredible treasures but is very poorly organised
13 1452008547
it is too crowded; so staying in a very close hotel to your distination
19 1452064810
bring a mask or something to protect yourself against pollution the car roads are congested and nobody seems to respect any traffic lights if they work it was amazing to see from our van how careless drivers are in the city    cairo museum was really good but you need plenty of time if you really want to see and understand many things labels are missing in many of the exhibitionsso use your imagination     the pyramids are quite impressive i noticed a lof of garbage around them a lof of people trying to get you to take a ride or to take a picture for money just say no firmly it might take a couple of refusals but finally they understand remember that the majority of  people get their income from the tourists that visit the country
63 1452069919
cairo is still a safe place to go and wonder around   if you are there try to visit kababji restaurant by the nile and najeeb mahfood cafe  prices were very reasonable and i had a pleasant stay  it was very easy to get around with taxis   visit the ritz carlton hotel downtown and have a meal the city caters for every taste
14 1452088430
under the edifying and friendly sameh our driver from the mena house our trip to egypt was extraordinary under trying times for americans to travel there  he kept us safe while he took us to giza and the pyramids and the sphinx and then to memphis and saqqara the tours he took us on were just exquisite and beyond words to be inside an ancient pyramid in saqqara and able to (gently)  touch the ancient hieroglyphs intact with  animal dye on its walls to ride a healthy and well cared for camel along the sahara to see ramses at memphis after hours to see how papyrus and oils are made sameh arranged it all for us even as mr trump disparages muslims everywhere he kept us safe and thrilled
33 1452100238
don&#39;t drive or ride in cairo  it&#39;s way too dangerous they are the worst drivers in the world and i have driven all over the world 1st and 3rd  world countries  watch your belongings and certainly don&#39;t trust any police not sure who are the bigger crooks criminals or them same goes for customs people at the sudanese border
52 1452105055
i like local cafe
50 1452181124
i dislike cairo
77 1452187059
meeting old friends and a visit to new heliopolis
40 1452188221
the people of cairo must be the friendliest i have ever met  we were welcomed wherever we went  they are quite rightly proud of their city and its amazing history    the stories of the pyramids and pharoh&#39;s is fascinating  the egyptian museum is like a treasure trove and the khan al khalili bazaar worth a visit for some real bargains
27 1452194241
all tourists areas are interesting to see food to eat kabab see foods  freindly people  difficult traffics all day time
14 1452350466
con man taxi drivers everywhere    even their meters have been tampered with! refuse to use meter and agree a fixed price before getting into taxi    other than the con men was great trip cairo is safest place to stay other areas are more risk of terror attacks like near giza pyramids sharm el sheikh and hurghada etc
33 1452366329
alhussaine and old aria
49 1452420033
انا حبيت المطاعم والجوا كان جميل
18 1452425217
i am egyptian so i can give my opinion about cairo regardless of my last trip duration cairo is the biggest city in africa and it&#39;s the capital of egypt
59 1452446935
we loved the guided tours we took facilitated by pyramids view inn be ready to negotiate everything and expect some people to flat out tell you that you did not give them a good enough tip this doesn&#39;t mean they are right but just be ready for it   i would be cautious with taxis even though we had agreed on a price negotiated by someone who speaks arabic the driver &#39;got lost&#39; or something happened so he seemed to be telling us he didn&#39;t know where the hotel was and driving us around aimlessly luckily i had the hotel&#39;s phone number so he could call and verify the location i would not have thought we were being hustled but when we got to our hotel the guys taking care of us said he most likely did it to score more money their location is very well known and right next to the pyramids   most of the time we ate at places chosen by our tour guides or hotel and the food was decent but nothing really amazed us for the price it was just fine though
15 1452508685
done everything i needed to do and see
75 1452510904
the people operating the tour services at the pyramids are awful - which made the pyramids experience less pleasurable    the khan el khalili open souks are great     transportation and food is cheap but be careful where you eat    so much pollution and so much traffic!     the nile river is not at its best in cairo    definitely you don&#39;t go to cairo for relaxation - it&#39;s a bustling city     all was good overall - go on low tourism seasons
4 1452523318
tour operator  was fantastic and very friendly in a country where other than those selling something no one&#39;s else seems friendly even the airport security and staff seemed incapable of hospitality our tour operator made us forget the rest and showed us a great time
79 1452662291
you can stay in cairo in downtown  get a guide
90 1452770772
busy city   too crowded   very and i mean very rude people
21 1452772362
the pyramids are why people go there and the museum    under no circumstances drive or ride in the place egyptians do not know how to drive they are flaming knuckleheads and they do not understand what they are doingcollectively is nuts     someone needs to show them how to run and organise a popular tourist destination     desperately soon
86 1452773487
although traffic is horrible in cairo one would be amazed by the serenity of the drivers and their coolness no swearing no shouting and no excessive honking   i ate at &#39;abou sid&#39; and in &#39;sequoyah&#39; both in zamalek and both serving excellent trditional egyptian food  the view of the nile from fairmont hotel was beautiful!
4 1452803750
the mall and cinemas and cafes
65 1455058455
cairo was a safe and enjoyable city the food the people and the sights were amazing! i cried because i had to come back the the us
46 1455062079
great stay
84 1455086695
cairo is a crazy city with massive changes underway the sights are incredible with huge diversity between the splendid mosques and the street poverty you still see people riding donkeys and living very basic lives everyone was was incredibly friendly and so happy we were visiting their country for us it was a pleasure to be able to visit egyptian sights with egyptian tourists but they would love more foreign tourists we always felt safe - there is security on all sights and hotels and using a guide was a huge advantage
81 1455120853
we spent 6 days in giza to see the pyramids etc 4 days would have been enough we then went to central cairo for another 8 nights 3 or 4 nights would have been sufficient     we would recommend going on a day trip  (or better still and overnight stay) to alexandria by train alexandria is more pleasant than cairo and the train service is excellent    tourists are hassled a lot in egypt and it is a pity that you have to be on your guard all the time having said that we also met many hospitable and helpful people    we are glad we went and saw the things we wanted to see but would not go back to egypt
63 1455186498
spinyx and pyramids tauts around pyramids are cash suckers they are very clever and shroud normal egyptians are very loving and respectful
9 1455189264
very nice and safe place
21 1455200621
important to stay away from high traffic stay hotel in new cairo
80 1455266673
i like every thing except this stupid motel that i was check inn  every thing in cairo very good lovely simple
22 1455315349
its busy noisy chaotic and i loved it i wish i had another week there just to people watch     if you like quiet beach resorts then it wont be for you but if you enjoy culture meeting friendly locals and a cup of tea with a shisha then you&#39;ll love every second
31 1455352519
things are cheap in cairo but a bit dirty
74 1455360945
great city with such a lot to offer
17 1455361701
pyramid and fayoum is a must place to visit the food is quiet good the people is quiet dirty the trafic is horrible but l like this place
95 1455382187
cairo is great as usual and never get bored in cairo
35 1455394500
cairo is just the most amazing city i have ever been to and i have been to many major capitals across the world including most of the usa  it&#39;s a lot safer than the media portray and a fun place to be  come and enjoy it!
42 1455446850
visit nile &amp; alhussain
71 1455447484
1 pyramids must be seen from panoramic view location the view is fantastic!  2 traffic jams are crazy and very frustrating it is pain to be in the car for hourseven for shorter distances  3 no need to climb pyramid they cost ep 200 per person and it is sheer waste of money  4 must take guide at cairo museum a great place do spend at least 3 hours
11 1455452808
pyramids antiquities museum old cairo and khan el khalili
36 1455456407
favorite places  the coptic places in old cairo  the citadel  the cairo museum   of course the pyramids   the monastery   pharaonic village    i have had dinner in several restaurants the food was really good and very tasty    if possible but i know it isn&#39;t the chaotic traffic😊
67 1455559064
cairo is madness! the noise on the streets and everywherebut it is a part of its charm so i did not mind ( a lot) :) egyptian museum coptic museum and hanging church are the must for a visit - giza and pyramids are not counted coz all goes there i loved the city!
77 1455614309
khan el khalili bazaar for its festive atmosphere and for its warring scents of insence and spices must try egyptian cuisine at abu el sid moloheya bamia kofta babaganoush tahini palate tickling cuisie uber is now available in cairo so its very conveniet to go around i wish international flights would ne in the new airport!!
7 1455632224
egyptian museum was enormous and interesting great trip out to the pyramids at giza taxis are really cheap but traffic a night mare   city itself is very dusty and quite dirty- lots of litter  it was an experience!
13 1455634146
beware of cultural and religious difference
99 1455647531
cairo is a chaotic dirty &quot;city&quot; with no infrastructure and no structure from the moment you land (until you get inside the plane and leave) you are being abused just because you are not egyptian taxi prices become at least x10 everything you try to buy is &quot;not available&quot; (unless you ask to pay double)  most importantly there is no bank in cairo that would exchange the egyptian money back for you especially in usd$!!!!! be careful!  avoid cairo if you can but if you absolutely must visit cairo make sure you are ready to be disappointed and make sure you can control your temper take a deep breath in your country and then go to cairo with a scarf to cover your nose and mouth (at all times outdoor) and enough antibacterial spray for the duration of your stay do not exchange money unless in a bank (back to your currency is not possible even at the bank but of you need to convert your own money into egp do it only in a bank) be careful of thieves!  i feel so bad for the civiization of the pharaons even if you make it to the pyramids they want special tips to actually show you anything or let you take any pictures   cairo museum is also another rip-off and you have to pay again another fee to access the royal mummies section  even the beautiful buildings of the beginning of the xxth century in heliopolis are falling into ruins  nothing is well preserved (just recently the castle of the baron empain)  it is a very disappointing experience
28 1455647708
cairo is a chaotic dirty &quot;city&quot; with no infrastructure and no structure from the moment you land (until you get inside the plane and leave) you are being abused just because you are not egyptian taxi prices become at least x10 everything you try to buy is &quot;not available&quot; (unless you ask to pay double)  most importantly there is no bank in cairo that would exchange the egyptian money back for you especially in usd$!!!!! be careful!  avoid cairo if you can but if you absolutely must visit cairo make sure you are ready to be disappointed and make sure you can control your temper take a deep breath in your country and then go to cairo with a scarf to cover your nose and mouth (at all times outdoor) and enough antibacterial spray for the duration of your stay do not exchange money unless in a bank (back to your currency is not possible even at the bank but of you need to convert your own money into egp do it only in a bank)  i feel so bad for the civiization of the pharaons even if you make it to the pyramids they want special tips to actually show you anything or let you take any pictures   cairo museum is also another rip-off and you have to pay again another fee to access the royal mummies section  even the beautiful buildings of the beginning of the xxth century in heliopolis are falling into ruins  nothing is well preserved (just recently the castle of the baron empain)  it is a very disappointing experience
13 1455687465
museum giza&#47;saqqara pyramids memphis coptic area - all dripping with fascinating history!  lots of interesting people sights and buildings all over the citybut you need to overlook the rubbish!
73 1455718967
every thing is ok  street is safe  lovely simple people  food is ok and al types are available  u will enjoy
5 1455731679
excellent place to visit and awesome culture would recommend every one to visit i liked the pyramid  would like to visit st catherine and red sea
17 1455787033
all of the historical places
49 1455796446
all time will be bzy in egypt
58 1455806942
it was a business trip so i was unable to explore cairo as much as i would have liked
76 1455810325
it is a busy city  with many touristic places to visit
37 1455816187
your experience is totally different as a tourist or as an expat - it&#39;s great for seeing the sights and for good nightlife if you know someone that will take you to the right places otherwise it is easy to get ripped off as a foreigner if you&#39;re not sure what to look for people are friendly and the city is busy
40 1459768278
beautiful city with  a lot of things to do and cairo needs much more time  we visited:(march - beautiful weather)  one day:  pyramids and sphinx   salaheddine citadel &amp; mohamad ali mosque   azhar park and eating at egypt studio restaurant  one day:   cairo tower  cairo museum  eating &quot;koshari&quot; at koshary el tahrir  one day:  pharonic village very very very interesting    we loved it;  but of course it was fast and everything needs much more time  and  next time we will arrange much more time  we want to:  try nile cruising &amp; dinner  khan khalilyetc  sound and light show and much more :-)
17 1459848334
many things to do and see shopping sight seeing relax dining  cairo traffic stays a challenge same like the taxi drivers make sure you have small cash on hand as they pretend they never have change and ask them to use the meter !
94 1459852656
glorious city with lots of history friendly people do be prepared for haggling and the big sell lots of lovely night spots by day there is so much to see and experience you need to get into all the little shops and cafes to experience the better cairo looks dirty and dusty from the outside but full of surprises and wonderful experiences cairo museum a must visit probably better with a guide as nothing much is labelled and not so well organised khan el khallili a little treasure trove of market shops coptic cairo amazing do bata for taxis they are very cheap here petrol less than 20p a litre try local food and tea
58 1459857777
cairo is the city for any travelerhistorical places need more cleaningi liked the eygption rest
70 1459865212
it could be cleaner traffic is just horrendous
16 1459867266
nice!
7 1459884595
very busy and noisy recommendation to find hotel in zamalek area pyramids sphinx  cairo towerare a must seeif you want to move in the city suggestion to find your own driver which can be arranged through the hotel or privately if you know someone over there driving is very stressful uber and careem are widely used  do not visit the pyramids and the sphinx without a proper and decent tour guide the people who will approach you at the entrance will rip you off you must try the egyptian food like koshary foul and tamiya hawawshy but from a decent restaurant only  must visit khan al khalili and must bargain a lot to get good deals try to visit the nagib mahfouz coffee shop that&#39;s a five star coffee shop with excellent food and winner of many food quality awardsoverall people are very friendly
47 1459966031
we spend 6 days with my son &amp; friends in egypt travelled by rented car from cairo to alexandria &amp; return egypt drivers are crazy &amp; dont expect any consideration to other drivers all fuel stations &amp; most of the shops dont accept card money so keep sufficient local currency pyramid museum &amp; alexandria corniche was good to visit
40 1459966295
cairo museum jizza khan khalili qaddoura   it&#39;s very crowed to move very expensive with low quality
94 1460021690
pyramids   a must see
41 1460022311
the hotel is is good value for the moneystaff is very helpful  its located i a crowded &amp; very old neighborhood that&#39;s the only drawback
94 1460032485
بلد جميل خاصة في شهر مارس وأبريل
94 1460041653
history and places to visit
18 1460051235
due to my limited stay i hired a driver must sees are the egyptian museum the old city the coptic area and museum then muslim areas and the 4 key mosques the main market and giza i loved staying on the corniche (the nile) and being near tahrir sq
95 1460051961
cairo is great city wonderful places in cairo
55 1460058532
camel ride at giza pyramids  visit of the egyptian museum in cairo with a guid found inside the museum next to the ticket shop  indian dishes at mena house hotel are delicious  our daughter loved the pool at the mena house hotel perfect end of a day spend in the heat and dust staff at the hotel are going out of their way to give you a great experience in egypt
61 1463997409
beautiful city a much needed see for all those interested in old history and culture that dates back millennia
43 1464004508
early booking saves you time
42 1464035500
nice and busy city
16 1464035756
nice city nice river and nice people
46 1464067483
cairo is has a lot of history and is great to see at least once but it is also quite dirty
62 1464083846
crowded you need a reliable driver to get around always trying to ripp you off and take the money wherever they can
81 1464084483
peoples are very friendly weather was  pleasant
76 1464090478
museums and shopping places
97 1464126035
friendly good food a lot of places to visit and attractions
68 1467739929
the fabulous cheap shopping i loved the cairo museum is my favorite place i ate inside the hotel from many menus given to choose from given to me from the hotel staff and also in restaurants all around cairo  it was very easy to get around cairo with many means of transport  eg taxi metro train  horse and carriage  buses 😃 the activities offered with the city were amazing  including  various trips on the amazing nile river
84 1467782639
cairo is amazing it has so many beautiful old buildings but sadly not well cared for the people cruising the nile with folklore music and dance  the magnificent pyramids the people richness and poverty the amazing people and some of them excell in trying to rip you off be careful   the cheap food sheds for pennies and the elegant and not so expensive restaurants  all in all great  tips wash everything barter with everyone be street wise and you will have a wonderful time  too hot june to sep
58 1464164106
beautiful chaotic charm egypt should be on everyone&#39;s bucket list
3 1467002463
don&#39;t go thro ramada nothing will be open before 1pm
30 1467011300
business trip not enough time to see around
72 1467097468
the media portrays egypt in a much more negative image than what i&#39;ve seen it&#39;s full of an exquisitely rich culture history friendly people and great food theres so much to see beyond just cairo though and i would definitely recommend not confining yourself to just the busy and traffic full city luxor and aswan are great for a tour of the nile and to experience the ancient egyptian history at its finest as well as the nubian culture alexandria and north coast (highly recommended- is more common for locals than tourists but has the most beautiful mediterranean waters i&#39;ve seen to date) hurghada sahl hasheesh and gouna (on the red sea) are also more known to locals but highly recommended if you&#39;re into sand boarding and camping (as i am) there are plenty of groups that host trips to the white dessert and places alike diving is most prominent in the red sea and sinai region and recommended is dahab or ras shetan and finally if your looking for a primative hut style living nuwaiba and taba are great and be sure to visit castle zaman! happy travels!
 */

/* output
92
28
28
28
28
33
28
40
13
28
28
40
28
28
65
40
28
6
29
40
33
28
28
67
40
92
0
28
81
4
67
28
92
67
81
28
58
48
28
92
0
37
28
40
67
28
40
6
45
63
71
7
53
67
2
40
40
62
3
92
92
33
72
7
28
28
29
38
29
28
28
33
13
28
71
98
68
48
33
13
28
30
93
48
28
70
38
40
73
93
38
28
2
96
10
63
92
 */

/* Input
9 1066
coventry
museum
shopping
transport
friendly
shops
family
restaurant
location
8   1460928546
it&#39;s coventry i wasn&#39;t here for the town
19  1460968240
beautiful city if a little jaded lovely people which made the trip a good one cathedral worth a visit the city centre is very clean
4   1460969206
a compact city with stunning cathedrals and lots of history the rising cafe at the cathedral was good with lots of atmosphere
5   1460972304
an absolute nightmare to drive around
10  1460982247
good shopping  easy to reach centre on bus for sightseeing and shopping
7   1461052548
very handy if you are working in the nec great locaiton
0   1461063287
dog showing so did not go to coventry
17  1461067850
city center  bella italia rest  easy transportations
7   1461067887
i don&#39;t particularly like coventry it just for me is not a nice city apart from the cathedral there is not much else to get you excited
2   1461069232
very pleasant and quiet city full of historic and cultural site worth of visiting hopefully next time if i will have the opportunity to visit coventry i could have more time to explore the city
8   1461071356
nice enough place with more going for it than i expected
7   1461071381
i was there for family bereavement so didn&#39;t see much
1   1461073694
transport museum always worth a visit  flying standard nice for a drink  plenty of new resturants to go to in the city now  visit to the cosy club was good
7   1461075476
it was in a good location near city centre and train station
8   1461079021
great arena
12  1461079703
coventry is good for shopping and there are plenty of places to eat and drink  driving can be a little stressful
17  1461091742
sunday evening and it was empty even supermarket was shut ugly post war buildings cathedral ruins beautiful but not much else to recommend it
15  1461093349
pub across road was excellent
13  1461104642
head up the cathedral spire and walk around the lanes there&#39;s a good curry house on sponne street along with some lovely pubs!
6   1461173494
loved staying in the lodge bed very comfortable room upstairs was a little noisy though  the bar prices were on the high side with Â£1595 being asked for 2 glasses of wine!
15  1461178224
dirty city hasn&#39;t improved since i moved away
17  1461185041
we weren&#39;t there very long - only for a weekend competition  travelled into the city for a meal - great commute - and really comfy stay in our hotel  we will be back
5   1461244253
dated city run and need of refurbushiment main city square and transport museum are the only places worth visiting
11  1461244381
only visited the ricoh arena for a conference so didn&#39;t visit coventry city centre
8   1461247122
did not visit centre of city very pleasant meal at a restaurant in meriden nearby
8   1461248393
the best restaurant:  aqua lebanese restaurant  cv13gr  we had superb arabic food and plenty of it with every dish service was prompt courteous and excellent had a lovely evening someone in our party left her handbag behind and it was returned intact the following day!
0   1461256651
the dirtiest untidy unkept hotel i have ever stayed in i cancelled the room and moved out
8   1460186125
we only spent an afternoon in the city centre good range of shops but unsure of what other activities available had a lovely meal at tumeric gold on spon street
13  1460191306
didn&#39;t go to coventry
16  1460204467
coventry is my home city
9   1460211904
very nice place
2   1460215040
the cathedral has &amp; is a wonderful testimony to god    yes although nowadays i am not good at walking the buses were excellent    very restful to sit in the cathedral surrounded with so many interesting things
17  1460223048
really enjoyed our short stay loved the cathedral and guildhall the free entry to the art gallery and transport museum was a bonus the shopping area is easy to negotiate and the staff at the tourist information centre were very helpful the staff at the railway station were also very helpful little things like that make a difference especially to those of us who are no longer young!
0   1460286763
nice shopping and eating out railway station easy walk and well served with trains be careful of taxi drivers - they do try and rip you off
1   1460288724
not much to say meetings
18  1460294291
did not visit
6   1460296362
love the area
13  1460317392
i like the rising cafe very interesting
5   1460374840
good area for sport shopping and dining
12  1460380088
we had to go to a meeting in coventry and the premier inn was the closet as to where we had to go
9   1460381171
when you first drive up the impressive driveway its omg!! wow is this where we are staying!! then you walk in the door and its omg wow!!  it simply takes your breath away  the building the furnishings those candlebras of every description lighting the stone walls every nook and crany has a delightful ambience that you just want to soak it all up but it is so breathtaking and impossible to take it all in  and then you get to your room and it is more wow factor  the staff are so friendly and efficient  we felt like kings and queens during our stay  our dinner was absolutely perfect from the wait staff to the food  thank you so much to making our first trip to england such a delight!!  this was in fact our first holiday overseas ever and this place just topped it off perfectly
11  1460393226
did not visit the city
0   1460396175
weekend was a treat to see a show at the belgrade theatre - perfect for having a look at the shops whilst being in walking distance
9   1460411059
coombe abbey
1   1460457134
meeting with friends
10  1460465131
nice university town fascinating mix of middle ages and modern urbanism
1   1460466327
went for rugby match not really to see the city
2   1460466792
easy to find your way around we went to cosmo which is an all you can eat restaurant and for me and my partner it came just under Â£40 definatly going to be going there again
9   1460467146
the cathedral and immediate surroundings are great that&#39;s about it!  service in pizza express was outstandingly good
11  1460468026
allez allez allez    wasps wasps wasps
13  1460473437
the transport museum is informative
6   1460474160
we did not actually go into coventry as we were visiting relatives near rugby
12  1460480118
very built up but close to stratford warwick and kenilworth the cathedral is worth a visit
0   1460480273
the motor museum in the city centre was excellent  went in at 10am when it opened  had an appointment at a pub and a game of rugby to watch in the afternoon so we had to race around a little at the end  we will have to return to do the whole museum justice  it is also free  think of making a donation with gift aid  free museums - it&#39;s one of the good things about living in the uk&gt;
18  1460483992
i was visiting family and taking in the local gang show
15  1463353903
loved ibis hotel the phoenix and an old pub full of different ales where we were welcomed by the regulars full of polite welcoming people! nicer than most places i&#39;ve been was so pleasant!
1   1463388126
did not go into coventry i was visiting an exhibition only
9   1463415066
i actually live in coventry and britannia was convenient to stay in whilst i moved out of one property and in to another - stressful time i enjoyed my stay the hotel has that touch of luxury but still needs a good lick of paint and minor repairs to the room walls the filth on the windows obscured any view and the building in general needs updating especially cleaning!there are plenty of places to eat restaurants and take-aways there are numerous shops although having lived here most of my life they are no longer exciting to me but great for new-comers
10  1463478604
we came to visit event in the city we had very little time to see it
2   1463490876
frankie and benny&#39;s terrible vegan options
15  1463490910
i only stayed one night for a pre - planned activity not far from the hotel therefore unable to answer this question fairly
15  1463493069
love the shops and the huge indoor market lots of places to eat cathedral and transport museum too
11  1463493526
coventry is a great location for exploring the warwickshire countryside and the midlands generally as it is fairly centrally located for both stratford and birmingham
3   1463495856
the grace hotel  the hotel breakfast at to wait for fryed eggs you order these but no one told us nice walks around the garden
10  1463499627
coventry fine
10  1463501984
not sure why this survey is here as i stayed overnight on M6 services
7   1463508482
visiting friends sat navigation recommended to get around city ring road can be difficult cathedral definitely worth visiting
0   1463510040
my main reason for staying at the village hotel is that my son and his family live less than a mile from it
10  1463512403
great town nice shops amazing car museum good for a weekend stay
15  1463513159
coventry has enough for young couples restaraunts cinemas etc is fairly easy to get around
16  1463515388
public transport links great
18  1463523238
good location for the city centre plus close to earlsdon for the blue mango restaurant which i would highly recommend
1   1463554429
close to solihull where our gÅ•andchildren lives!
19  1463562095
nice cathedral
1   1463562679
lots of building work in the centre of coventry  however outside coventry the countryside was beautiful
11  1463574671
shit hole
19  1458835453
leaving
16  1458897639
we spend our time at the ice rink and the leisure centre mostly good pedestrian shopping centre  we love nandos to eat!
0   1458933243
only passing though i was born and bread in cov moved to norfolk
13  1459002649
on business
13  1459040422
motor museum brilliant
15  1459085556
plenty to do and see bowling laser quest cinema eating out or in night life loads
17  1459085773
coventry revamping  itself and enjoyed the visit but not the hotel holiday in coventry
13  1459086846
the price was okay but customers should be told what you are paying for
3   1459092025
didn&#39;t go to city centre visiting university hospital
11  1459092107
dont go to pizza hut ricoh service is dreadful
7   1459095136
the herbert art gallery and museum was very good as was the transport museum and the cathedral there is a good shopping area but it does lack character we visited a good show at the theatre and there are plenty of places to eat
3   1459108237
i live in coventry and have done for many years
7   1459112825
i grew up here so i&#39;m biased :d
10  1459150769
went to motor  museum had a great time
12  1459159737
good road network
8   1459160330
visiting family plenty of places to go to millsys bistro earlsdon is always a good place to go
15  1459160388
the 2 tone village is a must for all lovers of the 79 uk ska phenomenum
17  1458490546
very comfortable good food nice and clean
6   1458559160
pretty shit in general hotel was atrocious glad i had my tetinus polio and diptheria jabs before setting foot in the hotel
10  1458560063
pretty ugly city should never have been rebuilt after the war hopefully soon the government will inject some money into it to make it a bit more pleasant but there&#39;s only so much you can do with concrete buildings
14  1458563384
we had breakfast at toby carvery before heading off to cake international at the nec
13  1458574162
cosmo great value food (buffet)  great pubs with real ale   great buses   so much history (yes really)
8   1458656499
great meal and service in frankie and benny&#39;s at cross point business park on a friday night we arrived at 930pm  we had a car and you definitely need a sat nav as there are loads of roundabouts  ten pin bowling and cinema complex on the business park
3   1458661944
a great university city     coventry hill hotel    coventry city easy to get around very lively but still serene in the evenings    good hotels but book in advance to enjoy good rates!!
4   1458662938
liked the pubs and locals very friendly! didn&#39;t stay long enough to see what else coventry had to offer
16  1458666007
i always go to coventry as all my friends are from there i like the nightlife places to eat and places to shop but mostly go for the nightlife
8   1458685413
not a lot to do here
12  1458739230
more information at reception and good service at breakfast there was no one there
12  1458818592
could be a lot more cleaner on the streets general amount of dog mess about the place on public foot ways &amp; foot bridge across ring road council could do better on street cleaning  i did enjoyed visiting transport museum such an excellent place to visit
16  1458821968
the flying standard  --food delicious and staff extremely friendly and helpful
9   1461676127
meeting friends again!
7   1461677901
coventry cathedral was really nice with a fabulous tea room  however the city seemed to be full of litter and was quite dirty  we won&#39;t be in a hurry to return
0   1461681658
awaful smell in the room price value for that room was not worth 85 pounds
2   1461682859
good night out
13  1461683233
stoneliegh park was a very good exhibition venue their restaurant was very friendly
12  1461733640
only went on the warwick university campus but we enjoyed our stay
2   1461770013
i didn&#39;t see much of coventry because i was at my brother&#39;s silver wedding anniversary it was very easy to get around because we used taxis
5   1461781866
only went to the hotel for one night as a last min thing
11  1461782043
ideally location for warwick uni
18  1461795562
coventry transport museum in the city center well worth a  visit
16  1461862302
i was there for a conference didn&#39;t see much of the city however a local taxi-driver told me &#39;there&#39;s nothing here&#39;but i&#39;m sure that can&#39;t be true!
12  1461923109
born &amp; bred there! growing &amp; thriving on student life but changing scarily like all big cities so much housing &amp; traffic football &amp; rugby teams need to move up a division!     if you are visiting there is a fantastic free entry transport museum plus an art gallery &amp; museum by the amazing atmospheric old &amp; new cathedrals also worth a visit:the old &amp; new rugby teams &amp; one historic football club that has to pay rent to play at the new rugby ground built for football there is also ice hockey (not historic) &amp; one of the best supported speedway teams at least for one more season two universities of course
5   1461931443
art gallery cathedral transport museum plenty to do and good pubs and good choice of places to eat
18  1461942069
time was short therefore i to have a looked coventry centeri liked there are many coffee houses i eat therei am very pleased about my travel
3   1459426078
awfull place
1   1459505476
i used to live in coventry and travel back every now and again for both business and family matters  the area around the station is going to be fabulous once complete
11  1459547456
warwick castle was absolutely fantastic
11  1459573488
nice place nice people
4   1459596600
over night stay for work didn&#39;t really see much
13  1459603716
lots of low end eateries toby carvery&#39;s steak pubs but lacking the finer places to eat coventry&#39;s become very much a university city hiding the gems of the old city sad ðŸ˜
3   1459620544
we were only passing through in our lorry on the way to way to france but it was comfy and clean
0   1459683776
the rooms are very dated and a bit scruffybut clean food was not to my liking
6   1459693034
nice town to visited
17  1459697124
the carvery was excellent
11  1459698977
didn&#39;t see much of coventry due to short overnight stay as going to nec birmingham the next day  we stayed outside in willenhall which didn&#39;t provide what we wanted regarding restaurants so had to drive further afield
17  1459702584
coventry is a lovely place just wish the hotel was better
19  1459713536
the herbert and the transport museum are well worth a visit as well as the watch museum all tell a part of the history  of coventry the new and old cathedrals holy trinity church and st john the baptist are wonderful historic places and a must see all within walking distance but very good frequent bus services too  lots of seating in lovely public spaces to take in the sights sounds and community atmosphere of a wonderful citywe had a very interesting three days and could have stayed another three and still not had enoug timewe will definitely revisit
6   1459714638
interesting market full of all different foods and culture
4   1459737970
evary thing vgood staff 10 out of 10
8   1462724610
ok lots of students quite loud at nights
13  1462726657
enjoyed the old coventry cathedral - didn&#39;t like the charge to enter the new one  there are a surprising number of shops so we did not have enough time to spend there  the highlight for me was the visit to ryton organic gardens  very interesting lots to see even early in the year  i would love to go back later in the year
7   1462779417
visited the cathedral for a graduation ceremony  not keen on the style of the cathedral - seems rather too modern  i found coventry city ok but again seems a bit of a concrete jungle surrounded by the ring road
9   1462792349
been to coventry bout 5 times never ended up on same road twice
5   1462792468
cosmo resaurant was fabulous instant seating (pre-booking) a great range of food    puddings could have been more varied
1   1462801230
to see my daughter at birmingham uni
1   1462810595
ideal location for local concerts or people working at the nec etc
7   1462817234
great shopping
13  1462874103
nice place
10  1462889686
we were there for a particular event and therefore didn&#39;t see much of coventry
5   1462889925
cleanliness and facilities and staff and serves
7   1462890721
the farmhouse is great if you like overpriced beer and terrible service  coventry is the asshole of england don&#39;t bother going if you can help it
17  1462892133
plenty going on but could do with better sign posts
2   1462895299
didn&#39;t travel coventry
14  1462899247
ate out at couple of lovely restaurants
4   1462899741
great history
0   1462902947
nice place if you stay in the nice area in the town centre near to restaurants and pubs
5   1462905437
lovely town with plenty to do and see
17  1462908824
i found it very confusing but friend i went to se says its easy i guess it&#39;s what you know!
13  1462910358
nice pubs
12  1462914588
just went for a family wedding weekend
4   1462918491
on a previous trip found that my hotel booking was for the wrong day and then no other hotel could accommodate me for over 150 milesi find it remarkable a city can be in a position at 22:00 on a weekday where there isn&#39;t a single room free to stay inspent the night walking between the 24 hour mcdonald and 24 hour gym in the town center
13  1462925490
i spent nearly all my time at the ricoh arena in the casino a good place for food entertainment and gambling if you wish
6   1462933493
as a vintage motoring enthusiast i would say local transport museum is a must see:)    i was really surprise of its capacity and the numbers of vehicles coventry cathedral does not need recommendation as its well known for being somber reminder of how this city was bombed in world war two
8   1462946961
easy to get to on the train or by car lots of shops and restaurants within walking distance of each other
16  1462952469
coventry is my second home spent a year for study there very convenient
4   1457016481
lived in coventry a few decades ago still easy enough to find my way around found a very suitable priced eating placethe ltiten  tree
1   1457021662
visited the football group for a conference first class service and conference
6   1457022844
worst inner ring road in the country
8   1457028729
i went to coventry on business and in the short time i was their i could tell it was not the best city centre i&#39;ve visited however the further out the city the better it seems to get and driving out there was some lovely buildings i drove past which were very easy on the eye
19  1457087567
lovely cathedral  i liked the fargo area  the ring road and orientation for different areas was a little confusing at first
12  1457194911
miller and carter  the cinema  very easy to get round and find your way
19  1457196144
road works made trip confusing and almost terrifying when arriving in coventry
9   1457277452
only there 1 day for a congress at the cathedral found the hotel journey in and out of coventry very easy and clearly signposted car parking was excellent for convenience easy walk to the cathedral and very cheap ie 6hrs for Â£10
7   1457374017
town centre is drab and  is all over the place shops are all on different levels ring road is hiddeous  indian restaurant visited on outskirts of city was however excellent  don&#39;t think i shall be racing to come back
14  1457383481
nice old buildings cathedral castles
18  1449152210
coventry is a dump i couldn&#39;t find anywhere decent for dinner so i opted for pizza express as it&#39;s &#39;the same everywhere&#39; no it&#39;s not my meal was really quite poor as is the rest of coventry
5   1449162728
i was born in coventry and was visiting family and friends i think its improving visit the cafe in the cathedral its fantastic the staff and the food we walked along the canal had a different view of coventry and it was peaceful with pockets of wildlife
3   1449414475
coventry is a city to pass by  center is drab and disappointing  numerous road works make walking from the hotel to the city center difficult
15  1449438861
lovely place to visit pleanty to see and do excellent shopping centre
9   1449501161
a great place for an evening out
18  1449595482
a fair amount of redevelopment is in progress probably not enough though the town has a seventies feel to it and in bad weather as we saw it its not a very nice place!  ate at cafÃ© rouge which was good had a drink at wetherspoons and found it difficult to find more decent establishments shame perhaps on a better day it would be different
4   1449604248
we only went to coventry to watch a rugby match
9   1449762540
plenty of choice for pubs and restaurants in the immediate city centre a fair buzz  student oriented and international campus feel to the immediate centre cathedral quarter close to hotel
3   1449914499
didnt visit coventry was travelling through
11  1449929487
avoid the coventry hill hotel
13  1449930085
coventry cathedral is worth a look there&#39;s been some civic gentrification but the city as a whole comes across as impoverished and post industrial
5   1449943158
it was an official trip and would not be able to comment about coventry from casual visitors point of view
19  1450018678
this was an overnight stay after an office party
5   1450018734
coventry is full of friendly people great shopping  cathedral is well worth a look along with the transport museum  stratford upon avon royal leamington spa kenilworth and warwick are only very short distances away
12  1450027632
very busy place but got used to it in a short time
12  1450029911
cannot say an awful lot as this is my home town anyway because of this fact i feel obliged to say coventry is great but it&#39;s really not the best place to go! the centre is a mish mash of varying architecture with buildings old and new and too much student accommodation! nowhere for us older generation to go for a drink!
8   1450030217
coventry proved to be a really nice city everthing central
13  1450083211

9   1450092910
great location near the city centre very friendly and hospitable people  great scenery and sights to see in surrounding area including warwick castle
2   1450111463
motor museum
11  1450114514
cathedral    art gallery and history museum    friendly receptioon at holy trinity church were we worshiiped ont the sunday
5   1450128867
trip was to catch up with family
4   1450188448
close to my son daughter-in-law and my grandchildren
12  1450188503
was visiting the nec
19  1450188608
better experience than birminghamless rates luxury comfort
9   1450188753
came to see my son at uni but found coventry a nice place to visit
16  1450189947
only visited the nec on a business trip stayed at best western windmill village great location for the nec and hotel has everything on site after a tiring day
9   1450201934
peaceful place
9   1450303687
a good fairly modern small city with a big and successful university plenty of eateries covering all tastes
2   1450375927
the cathedral was wonderful i was there on business so didn&#39;t get as much time there as i would have liked
16  1450392256
the cathedrals - old and new - are both worth a visit  the town centre is interesting with some fabulous old buildings that survived the bombing  recommend a trip to fargo village - art and craft area - and do try the bubble tea!university town so plenty of interest for young people  good eating options  city is very compact so easy to get round it on foot
2   1450394731
visit the old town and the cathedral the rest is dire!
15  1450423182
visiting family
0   1450445582
lots of interesting history
8   1450468499
meeting friends we ate at the golden lion inn easenhall which we have always found to be an excellent welcome food and service
4   1450486611
i&#39;ve never been somewhere with such a wonderful array of places to eat coupled with places of real beauty and historical interest loved the herbert art gallery and the car museum never fails to impressespecially with this christmas&#39;s santa ride addition! the cathedral&#47;s are both inspiring and breathtaking ate at the hummus house the noodle bar paratha wraps and weatherspoons took the advice of trip adviser for a lot of our dining ideas coventry is so easy to get your bearings the relative modernity of the majority of the buildings mean that there is an understandable layout took my husband to see my &#39;home town&#39; he can&#39;t wait to go back!
3   1450513617
everywhere
15  1450540746
pub was good restaurant was over priced noisy room and Â£120 for room only was not value for money
2   1450606507
it is nice small place to enjoy very beautiful shopping area
13  1450645389
enjoyment
1   1450655290
you would need to be on prescription medication to visit coventry for any other reason than a necessary business trip!!
3   1450715845
located close to town brilliant staff and excellent value for money
6   1450794332
it&#39;s got good night life and every we&#39;re is easy to get too
14  1450794378
belgrade theatre excellent surrounded by nice varied places to eat stayed at premiere classic in walsgrave and although it is a good location in terms of access to the motorway it is a very basic hotel not value for money
0   1450795732
the place is good but if your bed hasn&#39;t got the right mattress your stay is not a nice one to remember  they need to invest in better upgrade mattress
15  1450800437
we were only there for a swim camp but entry into coventry was mixed with the old and new with good architecture  didn&#39;t like the spaghetti junction style of getting on and off various roads easy to get lost or difficult to enter or leave lanes liked the fact everything was close to hand
18  1450874491
we ate at the wing wah chinese buffet and it was very good
3   1460490725
ok
15  1460491806
didn&#39;t see anything of coventry as we were taking part in a lacrosse tournament
16  1460492077
did not visit the city  nice meal at chiquito&#39;s near to the ricoh stadium
17  1460493479
did not go into coventry
12  1460493818
the coventry cathedral is the only site i would highly recommend
14  1460495405
great for watching rugby
18  1460532017
cannot rate coventry as stayed outside the city
3   1460537045
very good stay for the rugby at the ricoh arena
10  1460583272
great registary office where our friends got married much better than any other i have seen old and full of charm
14  1460637667
i came to coventry to visit my niece who is doing her phd at warwick uni the town is fine and ok and the cost of living here is cheaper than other places like it very much
15  1460639361
if not there for work would have taken more time to explore maybe next time
0   1460640088
good to see the investment in freshening up the city centre nice 15 min walk from the hotel to the train station
10  1460649310
you don&#39;t want to know what i thought about coventry!
2   1460707301
excellent city
10  1460724070
i can understand why it was bombed so much in the war!maybe we did it?
5   1460763260
interesting city brilliant university especially the cyber faculty
4   1460803570
cathedral is worth a look
12  1460821906
couldn&#39;t wait to get home!!
17  1460831586
i came for a business meeting so didn&#39;t have time for tourists trip
19  1460833967
beautiful historical city  the old cathedral is something to see also the motor museum is really interesting and it&#39;s free to visit it hosts some of the oldest forms of transport
13  1460835532
ok nothing special go to york instead
12  1460857292
coventry is like second hometown we&#39;ve been coming here since the last eleven years - our eldest child is a masters degree holder from coventry university - this time we stayed at ramada ate and drank there only - with few different times while shopping around city centre we ate from there
10  1460876876
coventry is one of the most booming cities in uk i liked the both universities the city centre coventry cathedral the food and unique hospitality
17  1460893082
lovely and calm place to visit with the family
9   1460900613
after you come out from the hotel turn left and about 20 yards there is a japanese restaurantvery good food and very friendly staff i would recommend it
12  1460902664
coventry is full of history
10  1460903275
went to meet friends as it was a central location
14  1460914339
great stay at the ramada
2   1460917651
did not go into coventry stayed to visit nec
1   1460918099
sorry cannot review coventry as we only stopped over night
11  1460920323
everything is bad  staff are unhelpful  everything i mean everything is dirty  linen is filthy   rooms are outdated   there is no wifi at all in rooms  wouldn&#39;t stay there again even if you paid me
16  1460921852
i had a meeting in central coventry and the location was excellent
0   1460924032
i was there for a scientific meeting didn&#39;t see anywhere outside the station hotel and the meeting centre
19  1462305085
good shopping
2   1462305495
the cathedral is the best thing about  coventry
4   1462310513
spon lane great for a night out
6   1462311684
we were there for a wedding
5   1462343138
if traveling without car recommend you take a taxi tour so you can see all the sights as we did coventry cathedral personnel were delightful and welcoming cathedral is a must see and their museum tour is very informative
13  1462343660
did not see much as at a swimming gala but would come back again good places to eat
14  1462346015
motor museum is worth going to stratford is very nice but expensive
12  1462352778
the people the warwickshire golf course and countryside
19  1462353054
the people the warwickshire golf course and the countryside
1   1462380109
conveniently situated for everything
4   1462400282
comfortable and good people skills
0   1462441995
coombe abbey
3   1462450016
great city
2   1462473853
city centre and meadow pool bus station
0   1462527488
nice place
15  1462562357
ring road is a nightmare !transport museum is fantastic well worth a visit and its free cathedral is interesting shops are varied and pedestrian friendly
1   1462572052
town centre
4   1462597485
some great real ale pubs hidden in the centre
5   1462632524
great breakfast nearby called &#39;the honest food company&#39; in allesley village
6   1462634533
very nice city to walk around sadly a lack of street signs
7   1462719351
having visited coventry before we had seen many of the sights this visit we were very impressed by the flower beds all around the city which were beautifully tended the blossom on the trees was also very pretty coventry is an easy city to get round with no hills to climb the centre was very clean there is plenty choice for eating out but turmeric gold is stunning!
12  1462720930
dull
19  1467024298
excellent staff and facilities
13  1467031056
it was a good stay for a gig! room i was in was nice and quiet and i even had a &#39;little terrace&#39; to sit out of my room and drink with company! very polite staff!
4   1467038202
we were visiting warwick uni
15  1467119449
great city
4   1467119531
the taxis are ridiculously too expensive
13  1467120954
we chose the hotel for  reasons like  its proximity to kenilworth  and henley  where we were attending a family event
14  1467121483
only their one day but enjoyed the cathedral
12  1467121685
most people were miserable and negative the organisation of the taxi service provided by the council for visitors after a concert and the ricoh areana was appalling! we queued for a taxi for 2 hours in the rain and we were one of the lucky ones as some waited for double that!
16  1467121707
we only visited the richo arena to see rihanna so didn&#39;t see much of coventry the pizza was good though ðŸ˜Š
3   1467121751
taxis  are ultra expensive worse than london
8   1467122319
we had a lovely time seeing the historic sights and cathedral lovely views
2   1467123801
found an excellent thai&#47;indian restaurant just round the corner
4   1467124502
i enjoyed coventry when i lived there
10  1467124727
ikea clean warm and great quality food and products
9   1467126204
transport museum
8   1467128243
hotel very handy for ricoh arena
14  1467129279
motor museum was brilliant it&#39;s free we spent an hour in there
6   1467129339
i lived here for 3 years for uni 20 years ago the city hasn&#39;t changed much like many cities it has the usual non-descript high street function university town - the uni now looks amazing!
7   1467130337
not much there to see the shopping centre layout is a mess the area isn&#39;t all that nice it&#39;s just an average place really
3   1467130846
attended a concert at rico arena nice restaurants and casino near by
15  1467130979
lots of history and interesting places easy to get around on foot or bus lots of restaurants we ate at playwrights and weatherspoons near the shopping centre the cathedral and st mary&#39;s guildhall are worth a look
14  1467134746
awful dirty place
0   1467137655
the city and surrounding area has so much to offer  as a disabled person who uses a scooter the city was very accessible
11  1457783120
getting around by taxi can be quite expensive if you can drive down there but you need an experienced or at least a patient driver to navigate the roads   so many places to eat   the shopping center closes quite early 5pm or 6pm i think so don&#39;t leave it too late to go shopping
15  1457783365
i&#39;m from coventry so i know the city very well and come back to visit family and friends there&#39;s loads to see the cathedral is a must as is the car museum
12  1457787590
studied there for three years nice to go back and visit old friends and attend the great british shooting show
11  1457791485
if you like beggers go to coventry!!
17  1457803020
lovely location for commuting to nec
8   1457830904
just visited nec
6   1457858605
no opportunity to explore coventry during this trip
6   1457862375
not my ideal areaa lot of traffic congestion
4   1457866169
stark reminders of the pounding the city took in WW2 and the new cathedral showing how it rose again from the desolation
8   1457878161
didn&#39;t go into coventry centre but dislike it very much from previous visits didn&#39;t feel safe there
12  1457879258
ring road a nightmare!
19  1457891805
i was in coventry to visit the university for my daughter we are londoners and never been to coventry before  we absolutely loved it the uni itself is fab  the city centre was a great surprise  it was huge with almost every shop you can think of plus a market  the layout of the centre is mostly pedestrianised so really easy to move around we had some great pancakes from the pop up stall - i had apple and cinnamon my daughter had butter sugar and cinnamon with chocolate sauce and my son had a savoury cheese and ham  - we all tred each other&#39;s and they were all delicious loved the area a lot  we all felt very at home in coventry we&#39;ve made this our first choice university jealous of my own daughter who&#39;ll be moving here and not me!!
7   1457892376
we actually went for the crufts dog show
6   1457896795
only stayed over for one night
13  1457946442
aqua lebanese  restaurant in the butts  is well worth a visit
13  1457960993
the food was not good as i am not a lover of instant mash and frozen vegetables and frozen deserts the room was awful dingy and not clean the only thing good for us was it&#39;s proximity to the nec not good value for money i have stayed at other hotels for cheaper costs and better service all round
9   1458027390
coventry is an industry city but still a green city the city has some nice old pubs and a couple of clubs to hang out if you have some time try to visit the country side for some nice trip stop by at saxon mill if you can quite nice pub they have back there :)
1   1458043010
except for crufts not much to say
6   1458050969
i was there on business so did not do any site seeing but what i did see i would not pick it for a holiday location
10  1458050992
everywhere is reasonably cheap great friendly people and a decent atomosphere
13  1458051476
we had been shopping in coventry then was nice to go hotel have our dinner and relax in room after
11  1458051799
we were there for competing in crufts so didn&#39;t have a chance to explore the area  there were a few eating places near the hotel &amp; lots of shops!!
8   1458052123
we ate at wetherspoons which was actually very nice  before going to the theatre  the town being a 10 min walk from hotel
10  1458052961
nightmare to even try and get a table in tgi fridays which is restaurant attached to hotel
11  1458055122
nec was fantastic
4   1458056939
motor museum and the market
15  1458058456
i am coventry born and bred so i was coming back to visit friends and places i know  its very easy to get around but then i know where i am going!  but the pavements are flat and there seems to be good disabled access everywheretop of anyones list has to be far gosford street with the medieval buildings on going - look out for charterhouse as well the cathedral and surrounding area and the museum - which seems to have upped its game a bit since my last visit  there is a lot of building work going on which is to the good  we ate out at bayleys kitchen in bayley lane - vgood value friendly staff and they almost cater for vegans (chick pea burger - still not sure!) but vegetarians well served and well as meat eaters  just be aware it is definitely a university city now much more than before and does cater for the under 30&#39;s as far as i could see but i am sure there is plenty for the older group if you look  all in all a good trip home
6   1458059891
ok for a short stay
19  1458060845
we travel to coventry every year to visit crufts or stoneleigh park so don&#39;t really know the rest of coventry very well liked the new shopping and eating resort at nec
16  1468400149
difficult to find nice areas and especially nice restaurants by chance i&#39;ve tried many times over several years there are a few decent places - make sure to use tripadvisor
18  1468439967
great real ale pubs
15  1468471406
for indian food just near is good resturant food is like home made
16  1468503873
coventry transport museum worth a look fantastic for young and old kids loved it and it&#39;s free
12  1468504246
didnt like coventry it seems very run down
13  1468521126
as i said earlier we were visiting family we did eat out twice one was an ember inn holyhead road not far from the hotel coventry it self is full of eating places    the cathedral motor museum    public transport is excellent    people of our age like visiting places towns villages cathedrals museum&#39;s castles we belong to national trust
2   1467544882
the cathederal is a must seethe motor museum is well worth a visit  attractions are all in close proximity
17  1467551666
actually very nice city very peaceful
9   1467559565
i defintely enjoyed visiting the auto museum
12  1467564449
coventry is a very good place to visit it has lots of things to do and plenty of places to eat
8   1467567730
the road out of coventry
15  1467572267
a beautiful city ðŸ‘ðŸ»ðŸ’‹âš½ï¸ðŸ»
1   1467572887
coventry was a hidden gem  lots of country side to explore
6   1467573088
lovely meal at the horse and jokey pub very friendly staff
15  1467588471
pubs
10  1467602319
the people in coventry are very kind and friendly
9   1467602657
it had been some time since i was last in coventry most of the changes i found were for the better the people are very friendly and helpful
5   1467612181
a good place for a short visit easy to get around interesting places to visit plenty of shops and reasonable restaurants and cafes
18  1467616394
lovely city to walk around expensive for food we ate in the bayley lane  some well preserved old buildings in spode street
12  1467625565
coventry festival was great ate at open arms friendly helpful staff
2   1467653432
came to watch the speedway but called off as the heavens opened thewetherspoons pubs were very pleasant; the flying standard just around the corner from the hotel
7   1467667742
baginton oak burnt post very easy route to coventry kenilworth leamington and warwick
2   1462000844
nice place
14  1462008263
because i was on a course at the university of warwick i never ventured into coventry
7   1462025465
cosmo
17  1462032272
cosmos offer great chinese buffet   at a good price    lots of concrete but some jewels
0   1462039963
it&#39;s an excellent location with shopping malls and shops for the leisure traveller and also many restaurants and eateries to match any palette the pubs were excellent and served good food with many variations the transport museum is one of the outstanding attractions it was easy to get around the shopping was excellent the food was tasty had a very enjoyable stay
4   1462044438
cosmo - buffet chinese restaurant excellent food loads of choice and lovely ambience
16  1462095778
don&#39;t spend money on a luxury room - it&#39;s no different to the basic rooms
8   1462123860
an amazing ice rink!enjoyed the old fashion market and ikea
5   1462134482
visiting family not coventry
15  1462136703
we went to coventry town centre
14  1462170016
i did not visit coventry the traffic officer i met was very kind and helpful the gps directed us to a wrong route and we could not reach the hotel he led us to the hotel
17  1462192078
warwick castle and stratford upon avon
13  1462213265
loved the restaurant at the hotel
19  1462262518
location was ideal for the ice hockey playoffs that we went for everything we needed was within walking distance great hotel
2   1462281406
good food and fab entertainment  and very modern accommodation
3   1462282697
we went to the harvester for food on evening food was excellent we went there for breakfast the next day which was excellent  as well   we went to the motor  museum which was free and we all really enjoyed it
2   1462284689
fantastic indian restaurant
10  1462284709
midland air museum stoneleigh car show
6   1462284711
being on my own with dogs felt very welcome and safe
8   1462285211
at he party at hotel
11  1462285360
easy to get around lots to see and do try fargo village for unique finds
11  1462287082
nice place to visit for a weekend visit the cathedral the motor museum and the art gallary
15  1462291512
we were visiting for a swimming gala
12  1462292350
top place great atmosphere in the city centre earlesdon high street was particularly good with so much choice of restaurants
19  1463614383
friendly historical city everyone was very polite helpful and pleasant the herbert museum and transport museum are well worth a visit and they are free did not realise the historical significance of coventry contemplating taking a professional post graduate course in the university after visiting
6   1463653976
good and quiet place
18  1463657545
very clean city centre
1   1463664845
nor a place for pleasure
14  1463672999
on foot coventry is easy to get around lots of history and great bar&#47;restaurants
13  1463675171
mount pleasant ph: great meal great service &amp; friendly staff ðŸ˜Š
5   1463679989
i was here fir course&#47;training so dud not see coventry as well as i would of liked on passing looks it looked clean tidy and no traffic living in london finding no traffic is not always the case
7   1463683029
there is a great toby carvery in binleyon the B4027 brinklow roadcheap breakfasts and very nice staff
14  1463684005
meduim size pool and steam room and just good facilitys
9   1463695801
this hotel is the worst i have ever stayed in staff were friendly but hotel itself should have been knocked down years ago it was a cross between a run down council estate and a prison
3   1463733503
the only thing going for coventry is the cathedral and immediate surrounding area
11  1463743914
all shocking and out dated
8   1463782799
nothing to add
11  1463816926
location - nothing wrong with hotel or location in belgrade plaza however local area was frequented by beggars and vagrants we had to take a taxi from hotel to white street coach park as we did not feel safe walking through the area
0   1463843637
beautiful countryside business trip so really did not see enough of it
0   1463864902
the warwick castle the cotswolds birkswell country side and bourton on the water are well worth seeing but stay somewhere besides coventry
2   1463915830
nice green town with parks and lakes
2   1463924017
the hotel good but hate the city centre seems to be becoming a depressive centre too many people stopping you and other people with clipboards etc
4   1463925383
working
13  1464187932
ok
14  1464199094
visited the motor museum which is fantastic for any motor enthusiasts i also ate at the cafe there all under one roof the museum is also free!!
11  1464213426
historically fabulous modern day terrible
4   1464213644
very pleasant city centre and of course the cathedral is a must i&#39;m embarrassed that we&#39;ve never been before
1   1464265252
nothing special
11  1464283010
great transport museum good shopping areasgood bus service
9   1464293096
business trip however hotel unadequate
5   1464334917
coventry is a reasonably cheap way of getting away from the chaotic london life and the weekend was lovely there :)
6   1464337019
although we&#39;d gone to coventry for a funeral service i found it very easy to find my way around  great shopping centre with easy parking and  plenty of affordable places to eat (two wetherspoons close to each other) i wish i could have stayed longer to see the cathedral with the line of old pennies on the floor and memorable wall hangings although i did see the famous clock with godiva and peeping tom
11  1464362854
went up to coventry to visit a friend who lives there so close to birmingham lovely place
14  1464419995
the cathedral transport museum coombe abbey that&#39;s what i like most!
6   1464427433
good things to see in and around coventry
1   1464452919
transport museum great adventure for everybody we will come back to see more
7   1464457043
the cathedral is worth a visit as is the architecture of the university
3   1464473714
the variety of establishments history and community
5   1464513899
excellent attractions such as the motor museum and air museum everything is easy to get to and is well sign-posted will definitely be returning to coventry again in future
15  1454318667
a great lively city with plenty of sights to see and great shopping  everything in easy reach
0   1454327029
shopping easy to get round good shops and market hall
0   1454345161
sorry it was not for pleasure we had to attend a funeral  our first nights stay was in a village called dadington which is so prettyhere we found the most gorgeous pub and stayed at the amionn hotel excellent room in a courtyard setting
11  1454436732
great variety of shops all within walking distance of each other
3   1454447136
i live in coventry used to be greatnot anymore
11  1454452704
meeting our new granddaughter and family
6   1454493269
the ska village which was the purpose of the journey  wonderful friendly people very interesting memorabilia  ridiculous value for money full english breakfast with carribean jerk sausages tea and toast for Â£400 !!!! served with a smile by the lovely angie then some ice cold red stripes in the bar served with a smile by the chilled out alf before an excellent live band the skaelectric in the same bar fantastic place for music fans
19  1454495080
didn&#39;t really see a lot of coventry as were only there one night for a family celebration at the highway club which was brilliant
2   1454495156
we didn&#39;t visit coventry but went to warwick and cadbury world which were excellent easy reach from here
7   1454508252
did not explore coventry went to a family party
18  1454577929
great location right next to a small shopping centre with different options of places to eat
3   1454578125
i was in coventry for a gig at nearby warwick arts centrethe city has improved since my last visit in 2011hopefully the building work near the train station will be complete and the traffic congestion will disappear!
3   1454601175
the cathedral is good
16  1454772838
it was where my work was
15  1454776120
sorry only stayed a couple of days to visit family
6   1454842442
too many road works and construction near A46 and A45 inconvenient at this period of the year!however the city itself is fine
14  1454853942
i live in coventry boyfriend lives in nottingham he doesn&#39;t like driving in coventry he did like the hotel though
6   1454858655
fargo village is awesome!
9   1454869668
everything was so superbstaff room cleaning location  all so clean and nice awsm food
17  1454956926
i was working there so can&#39;t comment
5   1455027004
fabulous italian in earlsdon
14  1455027634
transport museum well worth a visit   easy access to town
8   1455032854
visited coventry cathedral  visited the transport museum  had meal at asian (very good)  not impressed with tgi friday
4   1455036246
lovely city with loads of history
4   1455039251
visit to belgrade theatre which is a lovely theatre
14  1455039821
cathedral is stunning enjoyed the history around coventry excellent service and meal at wagama  everyone we met were friendly and helpful
17  1455042659
it was a visit to my daughter and grand-daughter we used to live in balsall common a village about 8 miles from coventry
2   1455060586
went to visit my daughter   avoid the ring road!   great lebanese restaurant in the city called habibbi&#39;s
7   1455095398
not a lot to do on a very wet cold day  transport museum worth a visit and of course the cathedral  good train links with birmingham and oxford which i found very useful within easy driving distance of stratford upon avon - much better
4   1455107811
transport museum is great way to spend 2 or 3 hours and its free!
2   1455201291
absolutely nothing to recommend on a tuesday night in february
13  1455203254
this was not a tourist trip - it was hard work moving an elderly relative into a care home definitely not a recommended pastime!!!
3   1455234004
for nec or it&#39;s birminghamyour choice!!
1   1455284404
coventry is a good place to be xx
7   1455352462
everything was great
6   1455387728
would not know it was for work
13  1455390428
went to business show did not go out into coventyr
2   1456670637
transport museum is great   besides that there&#39;s nothing much to see
12  1456671301
didn&#39;t really see much off coventry but it seemed very nice very big posh houses with electric gates seems a nice place to live  plenty of pubs and hotels
19  1456676313
great location for restaurants and pubs
18  1456677220
hugely underrated the cathedral is one of the best post war buildings in britain  the post war shopping precincts are also a huge asset which with careful conservation and reuse would be a huge attraction and could potentially expand the appeal of the city beyond that which it is currently attracting
0   1456683958
i went on a business in the centre it took me 12 minutes driving from the hotel pity i didn&#39;t have much time to discover coventry i would certainly come again
7   1456692200
transport museum excellent enjoyed the blitz experience  found the history around the cathedral really interesting learnt a lot  little you could not buy at the atmospheric market  great meal at bella italia  most places of interest within easy walking distance
7   1456700945
just popped into city centre to look at uni for old times sake massive change after nearly 30 years!
10  1456749613
it was easy to get to the town centre from where we stayed we found parking easy and free for blue badge holder as i am one  we visited the transport museum and found that 2 hours went very quickly
8   1456753777
i actually didn&#39;t go into coventry as has plans to go to warwick which wasn&#39; too far away
17  1456777307
pleasant shopping area not too big to walk around clean aswell  quirky bars&#47; restaurants - would recommend the rainbow inn in allesey  cathedrals impressive
5   1456827279
coventry is in transition with lots of road work in progress  this review is not a fair reflection on coventry as i only ever visit on business  this kind of review seems to be more about tourism  for those doing business or wanting to set up a business own a convenient location coventry is a very good location and there seems to be lots of investment going into the local infrastructure and facilities  if the planners do their jobs well this could be a really important and dynamic city in the future assuming the 60&#47;70s errors can be removed the old enhanced and the new used to compliment this dynamic image
8   1456843278
not far from centre of townhotel has lovely grounds and has good spa facilities
10  1456845561
coventry fine to get around i was passing through to go to stoneleigh
12  1456845724
ate at waggamama
13  1456849373
first ever visit came with my partner who is from there  not a great deal to see in the way of tourism but never the less had an interesting twist home to the story of &#39;lady godiva&#39; where a statue is on display in the city centre of her mounted on her horse  also you can see remnants of the old cathedral that was bombed by the germans in world war two a new version was built but does not do the old part any justice in my opinion  a good size shopping centre and lots of choice for a a drink and something to eat people are very friendly and helpful  a holiday is what you make it i&#39;d visit again definately
17  1456851404
i was attending an exhibition at nec so it was convenient
2   1456852451
museums cathedral and history with a little shopping on the side a bombed city destroyed by a motorway ring road and modern development but head to the cathedral or fargo on far gosford street for the coventry that is worth a visit
16  1456852556
i ran the half marathon great city
1   1456855064
it&#39;s very urban and 60s style (necessarily so but it&#39;s still ugly) i didn&#39;t go because it was coventry - i went for an event which was great  the fargo centre is interesting and bohemian
13  1456864288
great place to shop and museums great pubs and food
15  1456865823
nice city to relax and wonder around
15  1456874044
some interesting architecture good value parking ice skating venue
8   1456878974
lots of road works and awkward traffic islands
0   1456949578
the hotel was a nice little 10 minute walk away from the town centre where there are some decent shops dont get me wrong you i couldnt go into coventry as often as i go into newcastle but it was ok to pass the time we seemed to spend most of our time in the weatherspoons pubs when we werent out with the family but i would stay at the days inn hotel again good price for what you get!
11  1456953472
i thought coventry needs a total regeneration all 70&#39;s concrete buildings and totally depressing! we ate at the indian restaurant called akbars recommended by a local the staff and food were excellent we went to see the rugby at the ricoh stadium the reason for our trip as we are harlequins supporters
11  1456959086
very dull city centre good crowd support
0   1456996631
food we a ok in restaurant
0   1457003964
went for the half marathon and couldn&#39;t have asked for a better route  brilliant day and brilliant atmosphere  i&#39;ve also visited the transport museum which is free and well worth a visit as well as the cathedral
1   1457439192
so much to see and do  the transport museum is a must good cathedral ausome and engaging and the city is so close to other interesting towns such as stratford lemington and warwick  i&#39;ll be
8   1457446025
i came here for business wouldn&#39;t have came for any other reason
7   1457446174
i like coventry to raise a family in but not as tourist
19  1457446185
visiting my son
19  1457446226
enjoyed the cinema complex next door and had a good meal at franky and benny&#39;s
8   1457447858
lovely walk around the memorial park visited the cathedral and lots of shops all within walking distance brilliant car museum and free
5   1457448002
we travelled to twycross zoo was about 40 mins away   we ate at the restaurant the first night but then got kfc the second evening we had the breakfast there both mornings expensive but was good quality food   making our way to hotel was but  complicated as we&#39;re not used to that area but way out took us an easier way
4   1457448358
think the road signs to the west orchard shoppig centre need updting as roads have beeen change dto one and using a sat nave and google maps we still couldnt get to it having  gone down all routes wasent impressed once at the shoppong centre as half wasent let or closed  town centre needs a clear up and shops half closed  on the up side th coventry motor museum is very good  and free  on the whoe wouldnt go back to coventry  hotel good
2   1457454020
cosmos
0   1457460828
looking forward to the next time we&#39;re in coventry
0   1457464081
didn&#39;t see much of it this was just a quick business trip
8   1457465026
very friendly midlanders
9   1457465437
love the cathedral !
8   1457466268
people friendly
17  1457479618
dirty
3   1457528688
plenty to see and visit could have done with a couple of more days  will return again a stay a full week
13  1457578608
lovely clean city with great shopping facilities and plenty of plasces to eat out and socialize in
3   1457619761
road system confusing  lack of facilities
18  1457619774
fab!
1   1457622566
i live there
16  1457623251
business trip - hotel in a good location for the roads but nowhere near the city centre
18  1457642448
we liked warwick uni
5   1457692563
did not visit coventry per se just a short visit to a customer
7   1457692919
:)
1   1457698070
it is a friendly place with lovely spacious rooms that are lovely and clean the staff are helpful and very friendly a big downfall is that the windows are not double glazed and with the hotel being near such a busy roadway the constant traffic noise is not cut down at all the wall are quite thin so that you can hear people using their bathroom not always something you want to hear the food was lovely well cooked and presented but the breakfast was not that brilliant as we were at the later end of breakfast the items looked like they had been there for a while and were not all that hot
12  1457770148
no favourite place nothing to eat
13  1457775097
dinner dance
16  1466016747
coventry is not as big as i thought  easy to navigate with a good town centre and friendly people just watch out for the beggars
2   1466019444
cathedral is obviously worth visiting staff and volunteers there were very helpful the nearby art gallery was also very interesting
4   1466022368
never been before - very interesting city - easy to walk round - left car at hotel after trying the confusing routes in the centre and going round in never ending circles  first night ate ina marstons pub in the city after looking round the shopping centres  second day visited the motor museum then the cathedrals and then looked round the lower precinct  had dinner and a bottle of wine in the hotel later  very enjoyable meal
5   1466081531
lovely place with nice monuments
7   1466082743
as expected
13  1466084252
best was the easy access  worst was the poor or none wifi in the rooms a business client cannot do work in the foyer area !
6   1466095909
expensive taxis and rude drivers a 6 minute journey cost Â£17  recommend the farmhouse british curry house which was excellent
8   1466255506
city centre needs redevelopment
17  1466263493
did not visit coventry - we were meeting with family
16  1465908845
would only travel for specific purposes not ideal for vacation
19  1465909610
fantastic places to visit past and present plenty of shops to browse around and huge selection of restaurants to eat out at
10  1465910153
it a nice city
13  1465916993
the best part was the meal me and my husband had at the  middle middlemarch farm pub
14  1465922331
only went to stoneleigh show
2   1465927914
great city great shopping great food will go again
13  1465929108
we were visiting friends for the evening  who live quite near the hotel
17  1465930334
not great went to stratford instead
12  1465930958
came for a wedding
18  1465931571
came to a wedding
1   1465932278
did not look round as we attended a wedding
13  1465971914
excellent place to live easy to walk around clean has everything a visitor would need
4   1465972874
great place to visit lots going on and people very friendly  great city!!
0   1465983495
we went to coventry for a 30 th birthday party
4   1466010812
for us it was just an overnight stop on our way home
11  1466951063
coventry cathedral &amp; coombe abbey must view
9   1466959897
only visited kenilworth for a wedding
6   1466964632
very friendly loved the transport museum good breakfast at wetherspoons lots of building work going on but as we were on foot it didn&#39;t&#39; bother us good bus service to our hotel and warwick
2   1466971024
diverse place with plenty of things to see and do
6   1466985268
we did not get to explore coventry as the hotel was so disgusting we left
9   1467019157
we liked everything loads of pubs eating places parks shops good taxis and buses will be going again in october but not the hylands doss house
15  1467723994
attended a party met friends in cocked hat pub for preparing drinks
5   1467724104
the road layout is not the best  we went to the memorial park and the godiva festival  both of them excellent
19  1467725392
we went to  party so did not see much of coventry
0   1467725466
was not there as a tourist
16  1467726246
cathedral visit a must
8   1467727292
coventry is under rated and there is a wealth of old buildings and beautiful restaurants and cafe&#39;s to visit spend the time to look around this place
5   1467733557
horrible place will never return
7   1467735150
we didn&#39;t go into coventry but around it  great place to eat is the royal oak at brandon about a 10 minute drive
7   1467736375
seemed a typical city with good and bad areas
16  1467739322
we went for godiva festival the area we stayed wasn&#39;t the best (by the bus station in a hotel) a lot of alcoholics around even in the day we ate a lot at mcdonald&#39;s and burger king a lot of food places so you don&#39;t need to worry about food signs everywhere to give you directions making it easy to find your way around we loved sitting on the edge of the fountain in the middle of the town centre it was sunny and nice to rest our feet we constantly walked around the shops and found more and more the war memorial field where the festival was held was beautiful loads of benches park skatepark ponds - lovely walk and relaxing place godiva festival was free and fun! a lot of drunk idiots on the saturday night though - so avoid mass areas of people come this time if you&#39;re a family we also had a look around the cathedral and the university area which was nice average town
8   1467741013
very pretty
10  1467741770
we went into coventry city centre and the coventry car museum was very interesting and free to get into
18  1467742077
ricoh arena was great
15  1467744997
excellent place to visit with many places of interest wish we had more time to visit them all
9   1467756216
great central location for many other places like stratford etc
19  1467759366
nothing
14  1467788967
the walk from the hotel to the city centre via the cathedral is very nice    there are several chinese food takeout restaurants in the city centre
17  1467791562
the transport museum is excellent well worth a visit and also free    the city center is a dump crap shops and crap eating placesglad to get out     would not visit this town again if you paid me
6   1459861998
ol town starting to get some new areas
3   1459862011
motor museum was great
5   1459863368
i visited the university of coventry the cathedral and ate at cosmo my stay was amazing
13  1459863711
don&#39;t like paying for car parking at the hotel Â£5 a day should be free for residence  i upgraded the room and paid extra only to be told my a member of staff all the rooms are similar
13  1459863816
new roads in city centre confused the old &#39;tom tom&#39;
18  1459866193
favourite place is windmill pub great pub fantastic staff very tasty pork pies
2   1459868406
attended the hockey finals weekend
3   1459872951
histroical place with a good vibe
13  1459873470
good eating at habibi&#39;s (far gosford street) and noodle bar (town centre)
7   1459874257
just the price and the location
9   1459874670
whent stock car racing at brandon stadium
16  1459875793
no comment - we were visiting kennilworth  we ate at the pub opposite and had a very nice steak at very reasonable price although cleanliness seems to be a general problem around here
13  1459881630
we went to the skydome for the ice hockey
19  1459885383
was in coventry for the ice hockey playoffs!fantastic weekend everything is easy to get to and good town centre with lots to do
0   1459896626
beautiful resturants
17  1459928231
easy to get around the city centre - largely pedestrianised with lots of shops and restaurants including large chains wagamama nano etc also good selection of &#39;local&#39; can recommend tumeric gold curry house good food in an old tudor building
6   1459931122
some lovely historic buildings and cobbled streets plus an interesting transport museum
11  1459939669
lots to see and do in coventry at present but coventry is due to loose a major feature when the speedway and stockcar stadium closes in november
4   1459944451
nice city seems quite a lot to do
9   1459948533
it is a beautiful old citywith lots of really old buildings and written history to go with it mixes the old with the new very well nothing is too far to walk to and plenty of places to eat and drink long stay car parking is great and reasonably priced wished i could have stayed longer will stay again soon
6   1459974125
would recommend the cosy cafe for food and drinks really cute place and great service
14  1459975038
we ate at the old mill which was lovely we attended the skydome for ice hockeygood taxis
14  1459975080
the city centre is difficult to navigate unless you&#39;re familiar with it very busy traffic any time of the day leave plenty of time to get around
14  1460034238
i was a visitor coming to crufts
15  1460035712
in general i found the city centre a bit grimy the historic buildings were fascinating and beautiful but i was a bit put off by the run down feel of the place - it wasn&#39;t welcoming the ring road is a bizarre piece of traffic management that forces traffic to drive across eachother at speed which i found quite terrifying i don&#39;t think i would visit for much more than a day or so even if i had the inclination sorry
18  1460037417
town was busy but many east europeans roaming about in groups cathedral charged Â£6 to view the new building very annoying as its still a church expensive taxis cheaper to hire a car from birmingham airportwill have a good shopping area when completed
13  1460055639
ice hockey planet ice is 10 mins  walk parking is in a public  car park but for 4 days it only cost Â£1350 including  the weekend   wetherspoons  just around the corner  so breakfast  can cost as little as Â£435 with tea
9   1460102550
didn&#39;t really see much of coventry as travelled for a specific show
10  1460113483
not bad for the reason we came to coventry
0   1460117219
went for the ice hockey finals  not many bars around  not  much in choice of places to eat  hotel restaurant was nice (ramada)    inspire is worth a visit decent beers in the bar
8   1468329190
we came for ice hockey purposes  ideal for ice hockey but do wrap up warm
10  1468330108
loved walking around and seeing the buildings - lots of old buildings which had details of their history on them enjoyed the parks easy to navigate around loved fargo village and the shops there
11  1468330979
on ths occasion we were there on a family visit but there are some good atractions there eg both the old and new cathedrals motor car museum herbert art gallery etc
4   1468337438
having to go to birmingham then to coventry as it was cheaper than going straight there
11  1468341273
good centre
13  1468342514
coventry is a lovely city lots to see and do something for everyone
13  1468349318
i only stayed one night en route to london so didn&#39;t see anything of coventry traffic was horrendous though!
13  1468351063
the cathedral was worth the visit but nothing else unless you like shopping in shops found in every high street
8   1468362946
we wandered around the interesting ruined cathedral would have liked to go in the new cathedral but it was closed  good shopping centre and eating places
0   1466288797
enjoyed visiting the cathedral and the transport museum was really good
3   1466288799
nec
5   1466316618
did nt travel into coventry  drove to stratford upon avon one day  very easy 30 minute drive from the hotel  a very beautiful city
4   1466341752
coventry is sort of ok but almost a dead zone after six o&#39;clock
13  1466345253
enjoyed the park and ride facility as it meant we could view the surrounding countryside instead of negotiating traffic
9   1466355207
just awful
3   1466357954
this hotel was dirty untidy and uncomfortable and the staff were unsympathetic
0   1466359282
business trip so didn&#39;t see much of coventry
4   1466367030
a lovely historic cityplenty to see and do lots of open greens around city park land  nice traffic free shoppingi find driving in the city a dream as there are good signs to direct you around
0   1466405587
town was quite &quot;post industrial&quot; nothing special to see and full  of gray concrete
9   1466410591
could not get out of the hotel so did not get to see a single thing
8   1466411617
i visited coventry for a two day trade show at the ricoh arena the tesco was huge!
13  1466422895
not great from what i saw of it i would say quite a poverty stricken area of what i saw which was quite sad and shocking
14  1468164470
booked two deluxe twin bed rooms (following booking modifications) for which i received confirmation via bookingcom however on arrival i was told that holiday inn coventry south does not have twin bedded rooms (does have bed+sofa bed rooms) and they have had problems with bookingcom before about this apparently however staff were extremely kind and helpful but couldn&#39;t offer us different rooms because they were full and so discounted our meals and gave us complementary drinks vouchers to make up for the disappointment nice breakfast hotel is a bit dated re dÃ©cor (very 80&#39;s pastels but pleasant) no lift but only ground or first floor so not a problem for carrying luggage location close to our event destination so handy
5   1468168527
a visit to kennilworth castle very informative &amp; interesting
4   1468169764
absolutely appalling  food was worse  than a school dinner canteen  staff on reception&#47;manager unhelpful although restaurant staff did their best  unable to cater for dairy free despite pre booking was told all i could have for breakfast was a fried egg!!  no lifts working and put on 3rd floor  a really disappointing visit so checked out a day early reception staff didnt appear surprised at this and automatically offered a refund for unused night no recompense offered for appalling meal still waiting for manager to call me - who was supposed to at 3pm on saturday   do not book this hotel
6   1468177453
it is a very handy city to get to good train bus and taxi servicesplenty of shops places of interest  and people were very friendly and helpful
15  1468180252
the best place was the park by the coach station with the pond and ducks the restaurant where we had dinner was cosmos a little priceybut nice foodthe waiting staff was a bit slow and they kept dropping the platesi drove there in my car from the west midlands the satnav helped a lotbut there are lots of road worksso kept getting diverted adding more petrol to our journey i loved the library and the churches in the townonly thing i didn&#39;t like was seeing a few people begging for moneythe big screen in the square was also impressive
1   1468187801
no staff really arouind to help you  very poor room  puting guest in a newly painted gloss room very poor
6   1468222441
i was visiting my brother who lives in coventry  hotel was near where he lives so was ideal
17  1468224505
crap city
0   1468229796
the university  easy to get around  bus links are excellent
9   1468232577
coventry : the city you really need to find out about !
11  1468238525
ok
18  1468254232
we specifically went to coventry to see the transport museum which is great and only a short walk from the hotel we ate at the hotelwhich was good
8   1464990597
in house food was vgoodgrounds really nicestaff very friendly &amp; helpful
19  1465006728
didnt go into coventry attending a football tournament in warwick university
12  1465073104
recently refurnished hotel clean and with good facilities the kids loved the sky tv in the room   we ate round the corner at frankie and bennies as the welcome from the waitress in the grill was very frosty (we did try to speak with her twice!)   the room only came with 2 tea bags - we would have enjoyed more  breakfast was plentiful and there was lots of choice
10  1465120795
grotty city with direct traffic
1   1465128367
people were friendly good curries ricoh is good stadium for concerts
1   1465132562
poor transport to ricoh along with people not wanting to help you when asking and even being polite
0   1465132730
stayed over for springsteen town centre has had a much needed make over looks much better ricoh arena staff and ushers needed to be more pro active in directing post gig it was chaos
13  1465134384
unappealing town  i&#39;m sure there are nice areas though just not on my travels
18  1465135522
although coventry has a lot of history and places of interest we were there for a gig at the ricoh arena the gig itself was superb but our experience was spoilt by the lack of marshalling for the major car parks and took over 2 hours to get out and further hour to get back to our hotel because of overnight roadworks when it had taken about 15mins to get there and park up
17  1465135690
we enjoyed seeing coventry cathedral with the guildhall at the side through the archway free entrance and the surrounding architecture of the buildings also coventry has a lovely shopping area west orchards shopping mall and in the square lots of side exits and hidden gems of buildings dating back to 1800s statue of lady godiva in square there are so may eating places you are spoiled for choice and it is so easy to get around if you are traveling by bus if you travel by train there is a bus station for people who may have  difficulty in walking or a walkway which is easy to get to the town as it is well sign posted inside the town there is a bus station that can take you to other places like nuneaton and  leicester go and see yourself and enjoy the day there
18  1465136880
taxi drivers very rude
9   1465139038
bruce springsteen at ricoh arenaawesome !!
0   1465139625
a visit to the cathedral
6   1465141234
we just went to go to a concert at ricoh which was good
16  1465141492
unfortunately there was an accident as we got on the bus for the ricoh arena and if we had not got off and got a taxi would have missed the concert we caught the bus to go 6 miles at 515 and arrived at 650 maybe the police should have directed traffic better if they could have done
4   1465143371
first time at the ricoh for a concert brilliant location  easy to get to  fantastic facilities taxi drivers awesome
9   1465143894
amazing city!a lot of history there i just love coventry definitely is a place to visit
6   1465144419
we visited for a concert at the ricoh stadium we did not have an opportunity to view the city
4   1465146006
traffic management travelling away from ricoh arena after concert was a joke
5   1465146327
the transport museum just across the road from the hotel is well worth a couple of hours visit and entry is free !
7   1465147481
very nice clean city but we needed more time should have made it for whole weekend
6   1465148844
difficult to find car park traffic was a problem due to road works and because ring road was closed for an event
13  1465153248
cannot really comment only went for a concert
10  1465155711
we enjoyed the motor festival and the historic cathedral
1   1465155856
went to ricoh arena to see springsteen    train service non existent had to get a taxi and traffic awful    cafe rouge was nice
15  1465156272
did not go into covenrty only to rioch arena
11  1465156370
bruce springsteen who was fantastic but had to  wait for a taxi for over an hour  also my friend was in a car park and driving to the north east and had to wait for the stadium to be cleared before there car park could leave
10  1465157186
a very pleasant town especially the flower stall in the shopping complex top quality and the people in shops were very friendly would like to visit again
4   1465158200
warwick castle was fab
12  1465158883
transport museum a must
4   1465159951
only stayed in area to attend a concert at the ricoh stadium
14  1465162230
there was a car rally and roads were closed and the city seemed difficult to get around despite the road closures and around the train station area there was very few restaurants bars or amenities
15  1465164699
excellent guide at the modern cathedral was really stimulating he was like the teacher you wish you&#39;d always had coventry cathedral (built 1962) is worth a visit with one of the largest tapestries in the world    transport museum well worth a visit with a huge collection of vehicles and motor bikes
3   1465189351
too dirty
7   1465190112
went to see bruce springsteen concert  concert fantastic  traffic and organisation around event shocking 1hr 45 to do a journey of eight miles  no organisation in car parks after event 50 mins to get out of car park  worst concert venue ever!
1   1465191520
we were staying two nights for a springsteen gig at the ricoh stadiumtook about 15 mins in taxi on average very near to birmingham if you are thinking of going there too about 5 pounds on train didn&#39;t really do any sightseeing as we had to queue for concert !!
9   1465196083
only visited ricoh arena for concert but was great! make sure you allow plenty of time for the traffic if you go there we thought we had but it was a push we allowed 1and half hours from hotel to venue which would be plenty normally but with extra traffic only just made start concert!
13  1465197659
friendly people    ricoh arena has a train station- great idea  no trains running at 1030pm to get everyone home from the event - shocking!  bus driver who got lost on the journey from ricoh to the station and had to ask the way (when we were half way to warwick!)
7   1465205286
we visited coventry for the bruce springsteen concert at ricoh stadium according to maps the ibis hotel whitley appeared to be only a few miles from the stadium however traffic on  that side of coventry was horrendous - traffic lights (which were on exceptionally long phases) roundabouts every 100 yards on narrow main roads (which our cab driver told us was the main route to the stadium)  - nothing has improved in years!! 5 mile journey took 15 hours and cost  a fortune in taxi fare we had a great time at the concert but getting out and away from ricoh stadium is a nightmare a few signposts might help!!!  we left coventry at the earliest opportunity after breakfast next day- never to return! - and we will go to an alternative venue to see bruce when he next visits the uk even if it means a longer journey to get there
11  1465206336
massive event like the bruce springsteen concert at the ricoh arena and lack of transport taxis etc coventry cannot cope
3   1465211592
ricoh statium
7   1465212345
went to ricoh for concert transport afterwards was awful
3   1465214478
to be fair i did not see what coventry had to offer as we just visited the richo arena
8   1465239072
the cathedral town centre quite nice and not a long walk from the station also we were there unexpectedly for  moto fest which was very interesting
19  1465247475
only there for one night to watch concert
3   1465272517
coventry is an interesting city with lot to do and see and great shopping with the most wonderful pubs with great food
18  1465287080
nice
2   1467151319
we visited for a concert at the ricoh arena 4 hour queues afterwarfs for taxis unorganised ended up in the city centre and it was awful tatty didnt feel safe
12  1467191784
the purpose of our visit was to attend a concert so we didn&#39;t really visit coventry as such
19  1467213691
enjoyed the scenery and looking at schools  was easy to get around  people were very friendly and helpful
10  1467216260
didn&#39;t see much of coventry went to rioch to see springsteen
18  1467239871
beautiful town
9   1467270674
the auto show and the cathedral made the trip worthwhile  we were able to easily travel around coventry using a hire car there are numerous inexpensive eateries throughout the district providing a great range of food
2   1467270857
ok for short stay lots of road works and disruptions
9   1467278026
family staying to attend rhianna concert coventry town not for us!!
12  1467279776
it&#39;s easy to get to city centre although roads quite busy and there&#39;s some building works going on so there was some diversions coventry has a lot of history especially the cathedrals have visited the cathedral church of st michael great shops too! various restaurants to suit all tastes
3   1467281284
a dull city
1   1467282378
unfriendly bunch with nowhere good to eat
18  1467287458
location is very nice but bed not comfortable also is bit hot no air-conditioning
7   1467288729
i think the city itself was fine but the hotel spoilt it for me and my husband
7   1467292246
in coventry for business
8   1467294324
n&#47;a
1   1468072850
my business in coventry is centred around the airport so not much chance for sightseeing a bus service is available direct into the city from baginton and the cathedral is also worth a visit city centre was clean and litter free  the food in the old mill and also the malt shovel in bubbenhall is excellent access to all main road routes is excellent and will be better when toll bar end roundabout is finished later this year
10  1468077619
we only stayed overnight as a stopover before going further on our journey so can&#39;t make any legit comments about the city
17  1468095877
the cathedral has to be seen  the surroundings very nice
19  1468134495
we went for concert but great shopping
18  1468156418
won&#39;t be staying again
0   1453123274
coventry is a great place to travellots of venues and local  pubs
3   1453204993
my favourite place was the shakespeare&#39;s house and small town we enjoyed lovely food from hotel&#39;s restaurant yes it was easy for my to get there with a sat nav
1   1453212538
myself and my wife only stayed for one night staff very friendly and helpful the hotel on a hole needs updating slightly overpriced
1   1453213170
went for a business presentation at heart of england conference centre didnt go anywhere else in coventry
14  1453213430
i was in coventry for a specific event which was scheduled there because of its central location not a place i would normally visit
15  1453214941
average town centre good stay for concert goers or travellers who book events in the surrounding area coventry does not have much to offer but the usual touristy stuff
6   1453219894
i went to a family wedding previously visited coventry and all the tourist attractions there is so much to see and do in and around coventry there is a lot of history i can only suggest if you are going for a visit read up on attractions and places of interest beforehand to get the most out of your visit
10  1453227618
best indian restaurant is akabars but book in advance lovely city great shopping
16  1453232451
coventry cathedral is a must as well as the motor museum  everything is within easy walking distance and pleasant to walk around
6   1453232933
transport museum is great wetherspoons for tea oh and the men in the dresses give out free korans too
12  1453233036
only stopped at ricoh coventry hilton can&#39;t say about anything else
6   1453243433
i was born in coventry and visiting family and think coventry is a fantastic place to visit
0   1453244307
the white lion on wall hill road is a good place to have lunch or an early evening meal    i will never go back to hogan&#39;s bar and bistro  the evening meal was inedible
4   1453297286
great transport museum
15  1453322630
the driving is a nightmare but lots of public places definitely should visit at least once
14  1453397168
cathedral and spon street worth a visit  lots of places to eat  has good road system  only went for daughters uni interview and stayed one night - that as long enough!
4   1453492249
nice mix of old and new
1   1453569625
i was in coventry for business - no tourist biased comments
7   1453580451
catherderals  hotel  very easy  M6
1   1453622762
not too noisy
10  1453644911
very scenic i only walked around the corner to the university &amp; there was lots to see in such a short walk!   i need to return for a longer look-around &amp; maybe a knees-up test the night life!
4   1453646172
we were there for a family funeral so didn&#39;t do a lot of sight seeing
9   1453648537
coventry centre is ideal for shopping and cafes good cheap car parking
7   1453650221
coventry is small and easy to find your way around it&#39;s not to busy great historical places to visit in the town centre (fabulous cathedral) also the city centre is filled with young people as the coventry uni is located in the heart of coventry it&#39;s only two stops away from birmingham on the virgin train so if u do get board you can pop to birmingham
12  1453714513
good location nice restaurants opposite if you don&#39;t want the hotel restaurant (which is excellent)
1   1453794807
coventry is an amazing town easy to get around loads of places to eat in the city centre great for walking good number of museums proximity to birmingham airport and nec is great
6   1453801733
leaving coventry is usually the best bit
15  1453817201
i really would not recommend the royal court hotel to anybody it is a dive and the manager is very cocky ask for a refund and he told me know because i put an opinion on their page
4   1453818681
was only at coventry for a funeral we ate at cosmos restuarant  which was very good
16  1453820691
not a lot to coventry  smallish city with usual city centre late night shenanigans with drunken kids  on the saturday  good curry houses  transport museum if you like that sort of thing  wasps (the rugby club) have set up home at the ricoh stadium  all very new and comfortable but devoid of any character
15  1453825442
it was convenient having asads supermarket and mcdonalds  close to the hotel    the next time we will try to avoid to do things last minute  we will try to plan in time and spent more time
8   1453827609
have lived in coventry all my life and recently movedcame for a visitgreat city just don&#39;t stay in the coventry hill hotel
13  1453840261
coventry town centre
5   1453849584
was there for a seminar did not really have the chance to go around the area
12  1453890858
coventry has many sites of interest to see  lady godiva and the cathedral were high points of my stay
11  1453988520
the cathedral was nice
14  1454026656
the coventry transport museum is a great place to visit and get a real feel of coventry history it&#39;s manufacturing strength and its wartime history i didn&#39;t have time for other gems in coventry and warwick !
0   1454074550
there is certainly no shortage of places to eat!!!!!!! red dragon very good food we eat at various pubs including one harvester shopping centre was amazing driving around coventry is really hard with very short merging lanes!!!!!!!
14  1454093868
coventry city center was great the brands available were fairly priced
19  1454162711
nothing
13  1454216573
great transport museum
18  1454251329
i ate at nandos in walsgrave fab food   then over to the cinema all in the same complex   nandos  frankie and bennys  taybarns  showcase cinema  free parking   tesco supermarket
3   1454253561
we were visiting friends coventry is easy to get around
19  1454256850
we ate over tge road in lime!fabulous
15  1450967659
very clean helpful warmly caring please  if any problems it gets sorted straight away all friendly people
9   1450968985
i used to live and work in coventry (16 years) it has changed a lot over the years since it has become a &quot;university town&quot; now (i hesitate to call it a city any more as it isn&#39;t behaving like one) in the 60&#39;s (when i went there to train in electronics &amp; telecomms) it was a forward-looking industrial city rebuilding its centre (particularly the cathedral) coventry doesn&#39;t seem to have moved forward from then at all     i love the place for what it gave me and for the people i know who are still there unfortunately all the industry (for which the city was famed) has gone - and the road system is a complete nightmare    i was surprised to meet a lot of tourists - and hope that the city has not sunk into relying on tourism    good luck coventry - you have a lot of work to do to get back to being a leading light in the country
9   1451148047
we didn&#39;t get to do much because most places of interest are closed on christmas eve which was disappointing having traveled to coventry to see my grandsons
11  1451234752
bedroom breakfast very easy none
10  1451296673
the premier inn was good value for money
1   1451382237
not much to do or see
15  1461276290
overnight stay en route to york from bath  good distance and location from bath to break up the journey for our 3 small children  retail park within walking distance  breakfast sourced at tgi fridays restaurant next to the hotel was great
8   1461277230
the belgrade theatre for great theatre shows herbert art gallery car museum nice bagel cafe tasty food family run by opposite debenams&#47;west orchids cathedral spires cosy club
11  1461321739
ricoh stadium very good and i recommend a visit to the wwwtheartisanancoventrycouk went here when exeter chiefs played wasps and had a cracking couple of nights with michelle the manager upstairs
11  1461403638
a nice place in spring good people
6   1461443427
we stayed there to go to the motorcycle museum in birmingham which was great lovely place central to most things
9   1461495567
coventry is quiethistoricalnice for shopping  and sightseeing in the green lands around
13  1461503375
the cathedral
19  1461504759
a small city centre interesting mixture of medieval and modern
6   1461504913
town centre is shocking some nice restaurants in broadgate but the precinct lacks interest there are no interesting shops at all the most classy is debenhams and boots prevalence of phone shops and pound shops absolutely grim this is what happens when you put out of town shopping centres all around the ring road - the centre is dead
2   1461518443
unfortunately only went for family funeral so did not actually have any time for tourist visit
19  1461521981
probably one of the ugliest city&#39;s in uk - crack being dealt openly just next to hotel which was nice!
1   1461522558
not a sightseeing trip but helping a family house move  nightmare negotiating inner ring road after leaving m6 at j2 &#47; a4600 enroute to allesley
6   1461601184
went to visit daughter who is at coventry uni  found the hotel to be in a great spot close to restaurants  the room was clean and comfy and having booked in advance the price was good too
10  1461603164
used to live in coventry so know all facilities and tourist attractions good place to visit
8   1461604263
i was attending a family wedding reception and the st mary&#39;s hall by the side of the cathedral was beautiful  so is the rest of coventry that i have seen including the little cafe by the side of the cathedral
18  1461608673
born and bred in coventrywhat more can i say
18  1461612371
the city doesn&#39;t have much but it is mid way to other bigger cities good point to rest and then leave for another place
1   1465492194
nice place not as busy as birmingham
6   1465497914
it took 8 minutes from nec the train station is 10 minutes walk from hotel there is a great fish and chips shops close by yum!!!!
2   1465501135
we only stayed 1 night but wish it had been longerspent some time in stratford on avon as well lots of places to visit
19  1465504469
great motor museum cathedrals etc very central for surrounding areas eg warwick stratfordcotswolds etc
6   1465506899
a modern city centre with the usual collection of shops cafes and bars some areas recently renovated are lovely and sit alongside the few remnants of medieval coventry and the contrast between the old ruins and new cathedral  but like most modern cities there are areas that are pretty dire  coventry sits in the heart of warwickshire with easy access to leamington spa kenilworth warwick and stratford - all well worth a visit too
5   1465515627
did not see very much only sky dome
5   1465584069
i was in coventry for bruce springsteen show otherwise i won&#39;t recommend coventry not a nice city and not much to do or visit
6   1465641423
not too much traffic
15  1465755342
good for sport &#47; nice universities here
9   1465763315
didn&#39;t see much but lots of road works make sure your give extra time
10  1465799728
the cathedral is fabulous well worth a visit
5   1465842929
the farmhouse restaurant was absolutely excellent highly recommended
13  1465847801
was  up for my birthday  seeing my family  as i&#39;m from coventry  but now live 140miles away so to be fair i wasn&#39;t on a sight seeing visit
9   1465848345
just stayed at the hotel for 1 night and enjoyed the leisure facilties we had food at the hotel which was lovely
12  1459200101
jkj
7   1459257117
was on trip to visit family who live nearby
19  1459260256
nice place
12  1459261700
good range of shops and restaurants very easy to get to from the hotel did not enjoy the drive on the ring road getting to the otel
0   1459262707
not there long enough to go sight seeing maybe next time
12  1459266470
transport museum very good guildhall and cathedral worth visiting   eateries : my dubba indian street food restaurant excellent!
9   1459275054
the convienence of city centre with museum and cathedral at walkable distance
2   1459282283
very central for birmingham s stratford on avon  warwickshire near M6 M1
6   1459282403
the transport museum is well worth a visit and is free we ate at the DC6 restaurant at coventry airport i can highly recommend it food good not bad price and something different
4   1459283670
went to casino the restaurant was very nice
0   1459299452
the atmosphere was uplifting
14  1459315941
not easy to get around as a first time visitor but once in town everything is there
19  1459325472
nightlife and the national motor museum also surrounding area is beautiful
2   1459331034
not a pretty city but right next to historic warwick cheaper hotel prices in a convenient location some newly regenerated areas of the city centre with good shops bars and restaurants but recommend heading over to warwick 20 min drive or lemington spa 20 min local attractions include warwick castle and stratford upon avon isnt far away
3   1459356642
the transport  museum very good  the town cryer food was excellent and the staff were friendly  would  recommend it
12  1463941455
coventry it&#39;s self is a great place to visit
13  1463984969
beautifull green area nice for dog walkies
16  1463994969
good place to visit with country side not far away
17  1464001580
the armpit of the uk
10  1464003672
very nice place combining historical gems with modern areas
14  1464083217
easy to get around everything within walking distance of the hotel
13  1464083532
coventry itself was fine with plenty of things to see (if you like historic buildings)  unfortunately the hotel we stayed in was terrible which spoilt the trip
11  1464110953
no heating in room bed hard and old tv with few channels
3   1464134591
only went for a good friend wedding
9   1464168445
in coventry for a swimming competition but chose to do some shopping in the city whilst there and didn&#39;t feel safe at all there were refugees everywhere people begging in the streets for money who appeared to be of eastern european origin there were muslims pushing their beliefs on the quran and the edl elsewhere in the city surrounded by riot police and overhead helicopters i didn&#39;t feel safe walking round the shops with my teenage daughter and we cut our shopping short due to this only to be approached by a beggar at the car parking machine which made me feel very intimidated and vulnerable i also witnessed an argument over begging plots by the parking machine on fairfax street opposite the swimming pool between two men coventry needs to clean up its city pretty urgent feel very safe in london manchester and birmingham when i visit their but not coventry britannia hotel - next to the pool what a dump the owners should be ashamed it needs serious attention! did not stay here this is just my view as a passerby going to the shops
15  1464709872
cathedralsmotor mueseumherbert art gallary indoor marketa few shops
18  1464709955
for convenience and ease of use  blessed with a great location - ideal spot for a no   frills holiday
7   1464717376
one nightfor a wedding it was fine
15  1464717379
best of coventry was to watch a movie at odeon
18  1464718467
disgisting dirty hotel staff all unkemptfull of foreigne men not good for woman on her own
18  1464721076
we both liked having the service near by as we can obtained food and petrol  for a stop over retreat it was great no noise carefree and facilities very good value for money coventry have fantastic places to visit we both chilled
4   1464726751
it is just what i thought
13  1464743212
nando&#39;s restaurant
11  1464766150
the establishment pub for food and drink friendly staff coventry ring road very confusing tycross zoo value for money and close by for something to do
7   1464768686
nice place but too lonely nothing to visit or nice restaurant nearby
2   1464772259
great food at playwrights beautiful cathedral nostalgia at the two-tone museum junctions on ring road a bit scary when you&#39;re not used to city traffic coventry vip app for discounts very good
0   1464777010
from here we had day trips to warwick castle stratford upon avon and wellesbourne airfield outdoor market    coventry itself had a great selection of bars and eateries not far from the hotel    getting around was easy by car and traffic was not a problem
10  1464804286
beautiful city very clean and friendly lovely atmosphere!
9   1464810104
we enjoyed the transport museum
8   1464813568
the centre was not as picturesque as the fringes but catered for all our needs easily the hotel and restaurants nearby were excellent
2   1464876931
we only stayed one day and enjoyed the day visiting area and having breakfast a highlight was coventry cathedral and listening to the church bells in the cathedral
13  1464888368
great views of garden
11  1464898314
we ate  out at wetherspoons warwick we found driving around coventry very hard
13  1464939875
used coventry for base ie visiting friends
3   1464954525
did not see much of it as only overnight stay  the traffic system at the end of the road very confusing and easy to go wrong way down one way system turning right
6   1464970266
not applicable as attending a conference
4   1455448635
the transport museum was great!   fantastic albany theatre  that is maintained well to keep the vintage feel     taxi&#39;s widely available and buses from city centre
14  1455461437
coventry is a lively city plenty of eateries and things to do not necessarily all in the town either in walsgrave there is a complex that has it all bowling  nandos showcase cinema  frankie and bennys burger king laser quest toys r us and even a tesco supermarket premier inn so yeah unless you&#39;ve tried it don&#39;t knock it
16  1455461866
parking is cheap and easy plenty of good shops pubs and cafes
18  1455468281
visiting family and going to theatre visiting ikea store where we had lunch (sad aren&#39;t we!)
10  1455471080
great
14  1455480930
good old style romatic place to stay  good if u want to study and concentrate   nothing to disturb you   about 20 minutes drive from coventry   5 minutes drive from any restaurant   very calm place nothing to do other than enjoy beutiful interior and relax
1   1455528599
purpose of trip was theatre visit so only saw city centre  found it to be ugly dismal and utterly soul-less  the cathedral is about the only impressive building but is so hemmed in by hideous tall buildings that it&#39;s hard to find let alone admire  the shopping mall is spacious but only contains all the usual chain stores - nothing original  would not want to go again!
14  1455553421
the only good thing and venue was wedding at allesley hotel in coventry
14  1455567664
we only used hotel as an overnight stay
9   1455574616
coventry cathedrals (old and new) an excellent guided tour of the new cathedral included in the admission price free admission to the transport museum good rugby both at premiership and division 1 levels    the town wall pub is recommended for good beer although finding a table for food can be difficult at weekends    coventry is not very pedestrian friendly in places but well served by rail    i would probably visit in the spring&#47;autumn next time
8   1455629141
the nature is very nice  people are friendly
14  1455631780
i live in coventry and it has a good shopping centre with plenty of places to eat and drink there is a lot of history so it is interesting to see things connected with this
8   1455632114
casino and the richo shopping centre
9   1455633958
even the service at macdonalds was the slowest i&#39;ve ever come across
11  1455637647
the cathedral area is great but  you dont have to walk far to an underpass or residential area to see litter strewn broken bottles and empty cans  we had room service as we were barred from eating in the hotel ramada because we were not valentines  will avoid staying in city centre next time
1   1455637786
lovely place i will visit again and look around places
0   1455643365
cathedral and ruined cathedral beautiful priory ruins and undercroft fascinating had great tour by man in visitor centre fascinating to see what escaped the bombing
15  1455643408
liked the midland air museum transport museum coventry cathedral and the central wetherspoon pub
12  1455644351
yes easy to get around
8   1455645177
easy to get around lots of available taxi&#39;s  friendly bar staff
4   1455648247
good value shopping  enjoyed a good couple of hours going around the transport museum which was also free to go around
1   1455650469
coventry is as good as it can be
16  1455654507
i travelled with my son&#39;s to watch wasps play excellent hotel near to ground  my only gripe would be paying for parking i would have thought it would be free for guests
12  1455657478
only went for a car meet so didn&#39;t do much but the coventry transport museum is a must go free entry and lots to see
10  1455713460
yes going out from the hotel
13  1455714967
i wouldn&#39;t visit unless it was absolutely necessary not a recommended city at all
16  1455715489
we were there to celebrate my father&#39;s 90th birthday so didn&#39;t do the touristy things
4   1455727733
we only stayed in coventry as was close to visit warwick the next day  i would never return to ccoventryempty nothing going on there hard to find a good place to eat  just  shops and chain restaurants   found a nice bar in old church &#39;inspire&#39;   man on bike tried to sell us drugs  not impressed
5   1455783239
much more than you expect - great history and museums - fargo creative village  is a treat - sort of camden&#47;brick lane in historic street being regenerated really diverse and lots of young people give it a vibe great restaurants too in priory place - part of interesting historic core and remains of godiva&#39;s cathedral
0   1455792820
good takeaway food that can be delivered went on a business trip so didn&#39;t go out much  good shopping strip near the ricoh centre
15  1455801155
the music museum is a hidden gem
4   1455805236
traffic system a nightmare go fully armed with map&#47; sat nav and traffic news
17  1455807459
nandos
19  1455816753
we only stayed one night  and left early the next day  we used this hotel as it was half way to skegness  which was are destination
4   1455817183
only stayed at the hotel it was a lovely overnight break
7   1455820899
stoneliegh park
11  1455831260
the cost club was the best place lovely food great staff it is not easy to drive around coventry
11  1455877862
eat at royal court the lovely &quot;country house atmosphere is relaxing any help you need is there for you
7   1455883004
city is rather ugly and late 20th century in a bad way had a great meal in the golden turmeric
3   1455885531
did not visit anywhere really as had a civil wedding at the hotel
2   1455909723
didn&#39;t really go into coventry but tamworth nearby is a great place for families old and young alike kids love the snowdome and the park opposite which leads you to tamworth castle sure is worth a visit nice shops quaint and quiet a lovely place
18  1455986004
i&#39;ve said the time in coventry is okay but we really only spent one day there the rest of the stay was in birmingham the transport museum is really worth a visit particularly on a wet day
9   1455987306
we were visiting relatives
6   1455987370
it&#39;s a good town but more needs to be done in terms of providing good hotels near the city centre as most of the hotels nearby are appalling!
10  1455995312
the cathedral is beautiful  however the city centre appears to be dirty - there&#39;s a lot of rubbish and dirty pavements
17  1456064254
free entry to transport museum and cathedral had lovely lunch in cathedral cafe visited theatre
0   1456065273
it was ideal for work
9   1456067073
spon street for food and drinks was good the cathedral is lovely and worth a visit otherwise the city doesn&#39;t have much to offer one night - tops!
14  1456074989
came on route to the university
15  1456076490
coventry is a really nice city it has plenty of places to eat good quality vegan&#47;vegetarian food some of the pubs&#47;bars are really nice too it has some interesting buildings and shops and the market is greatfargo village is also an interesting area worth a visit     many of the buildings are very attractive and it has a lovely atmospherethe ring road makes it a bit tricky to walk around and along with some ugly modern buildings makes the city less accessible and attractive than it could be however this should not put you off as there is plenty worth seeing    overall i was pleasantly surprised as i feel coventry is often overshadowed by other cities in the midlands and you don&#39;t hear much about it well worth a visit
19  1456077823
parking could b free   had room service
4   1456081253
i went all over from the albany pub where the specials originate from the coundon hotel and the sky dome strip it was a great night a good laugh with friendly people
15  1456112105
it was my first time there and i have friends who live there that escorted my family around  there are things to do on foot as well as by car  value for money exists in visiting the transport museum and local sites for pictures and information like coventry cathedral  if you have a car to explore needs be one must go to the space centre in leicester  we enjoyed this greatly  when we next return to coventry we will be going to visit statford upon avon
18  1456132680
friendly staff very clean and comfortable and excellent value for money
10  1456142414
room looks outdated tv didn&#39;t work at all
8   1456152812
was on business so did not see much of it
12  1456153450
did not see any of coventry was on business but am sure its a nice place
11  1456164538
the main church and the old cathedral were wonderful along with the art museum the new cathedral a disappointment poor area with lots of pawn and charity shops the people were all nice and friendly and everything in walking distance the best part was the transport museum good gift shop there and nice coffee shop in the museum by the university premier inn and other hotels nearby and it is near a station etc with lots of buses central to the town be careful of the advertising for the brittania royal  court hotel looks like you are getting to stay in an expensive hotel cheap the grounds and hotel are beautiful and the food good and cheap but it is serve yourself and the rooms are old and tired no toiletries provided not clean they also boast a gym sauna pool etc but this is also old and not very clean floors filthy main issue they host dog events in large numbers so dogs get to stay in the roomsnoisy and dirty
19  1464525145
i am a frequent visitor to the city centre but this was only second time in allesley area (jacobean hotel) i found it amazing that the hotel is on a busy main road yet the back garden backs on to fields with horses and it feels like being in the countryside  also spent a fun time playing crazy golf in allesley parksure it would be a lovely place on a sunny day -however on a cold windy day it was a pleasant surprise to find the ice cream van also sells teacoffee and hot chocolate  if you haven&#39;t got your car pop into poole meadow bus station (city centre by transport museum) - loads of leaflets &#47;bus timetables and very friendly staff to get you to other towns or even the incredibly frequent journeys back to the train station
8   1464527504
thought the city was good and getting around by car was fine the car park was old and badly designed for such a busy city but overall would visit again
9   1464527704
only went to the ricoh arena but would have liked to have explored coventry centre more
17  1464531376
went to ricoh arena for a concert it was brilliant
15  1464532289
we went to the ricoh arena for an event besides them taking all our toiletries off us (deodorant &amp; perfumes) we had a good time    we ate at indigo an indian restaurant it was amazing the best indian food we&#39;ve ever had! the staff were great too    there are a lot of shops in the city centre nothing special     we could not find anything to do in conventry besides shopping and eating
17  1464535373
the cosy club
5   1464537372
good
4   1464538047
lovely place lovely people! would definitely go back :-)
12  1464540048
didn&#39;t travel around coventry at allwe used the amenities close to the hotel
4   1464546566
visiting family enjoyed warwick castle
0   1464548991
lovely convenient city close to the motorway
4   1464551485
we visited warwick arts centre to see dinosaur zoo live! my six year old loved it  not the easiest place i&#39;ve ever driven but we got therein the end
8   1464558380
loved the motor museum and old style market great places to eat lots of interesting sites within a short distance
0   1464625638
it&#39;s a dump!!
4   1464682892
the was lots to visit locally we ate both evenings in the hotel food was excellent
8   1464684003
i was born in coventry so i am biased
9   1464699968
ok without young children
11  1464700773
try punjabi hut restaurant of you only looking vegetarian food
11  1464701056
the location is really good  out of the city and at the same time very close  nice and green
1   1464701076
surprisingly clean and well kept lots of improvements happening by the looks of things landscaping and traffic calming chilled out plazas and lots of busy shopping precincts with more parking than you can shake a stick at i like the fact that they haven&#39;t pulled down all of the 60s architecture instead they&#39;ve cleaned it all up and put it all to good use    don&#39;t know where everyone went in the evening though didn&#39;t see much in the way of night life guess that must of been somewhere else
12  1464702395
average
9   1464704326
didn&#39;t like anything the pictures on website do not reflect the real place
15  1464705676
a short trip for a football tournament so didn&#39;t get to explore much but the tournament was brilliantly organised and the university looked fab
12  1464707468
quite location and easy access
4   1464708289
didn&#39;t go into coventry so can&#39;t really saywarwick is amazing though!!!
5   1458066311
coombe abbey hotel great place transport museum and coventry cathedral very nice good shopping centre
1   1458067222
great tour of coventy cathedral - worth the money to support the upkeep of an iconic blinding both old and new  dinner at the establishment great historic pub good atmosphere
17  1458070316
only there for a gardening show wouldn&#39;t be fair to rate anythingmill inn brilliant good and hotel brill
4   1458070687
reoch &amp; casino
10  1458071868
i was surprised how pleasant coventry is i like the open spaces cathedral area interesting and piper&#39;s windows stunning
17  1458075488
i come from coventry so obviously this was my home town
19  1458075640
stayed here to visit the world famous crufts dog show and was pleased with the location of our hotel there were numerous places would could have visited locally but didn&#39;t have time plan to return and make time to visit these attractions next time ate in the hotel and the food was excellent found a lot of roadworks in the area which resulted in us taking the wrong turn on more than one occasion but will definitely come back
13  1458121912
it was ideal for our visit to the nec birmingham it was nice to see they allowed dogs in the rooms    large comfortable and very friendly
18  1458139042
didn&#39;t feel safe
11  1458145790
parts of coventry were ok
8   1458229306
my trip was for work and except for the new cathedral coventry does have much to offer the tourist but nearby there are many places such as warwick stratford-up-on-avon the cotswolds and london is not far away nor birmingham airport
17  1458230663
i have no complaints!just stayed one night in transit from essex to herefordshire  i had to walk over the motorway bridge to get a fully cooked breakfast otherwise all excellent
6   1458234468
a very dull city with little to offer fortunately i was only stopping over night there are some lovely towns like kenilworth nearby
18  1458243305
didn&#39;t go into coventry but was very handy for the nec about 15 mins drive
8   1458249546
coventry is a challenge to drive around and through
19  1458258816
ricoh arena very easily accessible from M6
10  1458287407
the hotel was just ok not great
18  1458295243
easy to get around
0   1458332546
transport museum well worth a visit easy to spend 2-3 hours there
0   1458337407
our short visit was confined to the hotel and grounds
15  1458341709
only visited the ricoh  grateful for new trains that go to coventry arena
7   1458372169
was there for the motor museum and also a quick look around as a town i preferred birmingham      for the museum its worth a visit
0   1458375312
nice place
14  1458395378
i probably wouldn&#39;t choose coventry for a short break - but if you&#39;re there for another reason (in my case visiting family) it&#39;s worth taking in the town centre art gallery museums cathedral university of warwick arts centre - and it&#39;s within easy reach of good countryside attractions and other towns (stratford warwick oxford)
18  1458397097
the local restaurants and public  houses were very good places to have lunch or tea
8   1458422420
i have said eveything although the check out time was fantastic
3   1458485001
bet nice
0   1466515330
ricoh arena
5   1466523486
its amazing what good urban regeneration can do coventry has embraced its strengths and is now a wonderful city with culture and night life the shabby 70 concrete feel i knew it for has been updated and now has a wonderful environment full of shopping life and culture its not a city to stay in for extended periods but for a weekend or as a base to explore the midlands i would recommend it
1   1466523532
i didn&#39;t get to see  it all but what i did i enjoyed
13  1466524092
the staff were very friendly no matter what time you went to the desk there was always a smile they went out their way to make your stay as good as it could be  the hotel itself is clean and tidy the parking is excellent  the food is lovely and prepared to your liking
1   1466525512
to be fair we didn&#39;t see very much of it but what we did see seemed a little tired and dated
8   1466529560
was there for working purpose so didn&#39;t see that much but in this area were the hotel is was lovely( royal court hotel ) coventry easy to get to from the motorway network
13  1466531451
located within easy travelling to rugby - the road works only added a short time to any journey
7   1466532337
there is lots of eaterys within walking distance   for a taxi to the middle of coventry is between 12 -15 pounds
0   1466535058
coventry is a very beautiful city shame there arenot  more shopsin the city  insted of students accommodation
19  1466581170
great little city centre
17  1466584009
great short stay whilst en route to stratford
6   1466584902
we wereat the motorcycle museum for a wedding and did not go into coventry
8   1466597935
quaint historic town with lots to see
4   1466612941
city centre shops ect
2   1466625959
we stayed overnight had tea and breakfast both were lovely and place had a lovely welcoming feel we travelled to twycross zoo from the hotel as its only 30minutes drive we plan to return soon to visit other places of interest near by such as coventry city
5   1466667122
not a lot to do unless you drink a lot
18  1466667289
curry houses and pubs in old part very very good
9   1466692491
went to be near hospital as husband have big surgery and live 30miles away stayed over for 2nights to save travling on bus train
9   1466693711
i was on business and had no time to go out and about
16  1466713245
coventry is a nice place places to eat places to shop and places nearby to visit such as warwick which has a castle market place and st mary&#39;s church leamington spa where the guide dogs are trained and a shopping centre
14  1466859768
for work
0   1466861374
we were there to visit the university but managed to take in some of the sights and ate out a couple of times really interesting place    we ate in wagamma&#39;s which was really good value and great food   coventry is a good base to get to places such as warwick and stratford upon avon
16  1466865658
central location easy to travel to from london
18  1451998213
earlsdon nights out are what we like  stayed for new year
5   1452005531
great transport museum
17  1452007333
star city was only 18 miles and it&#39;s packed with activities for all shopping food entertaining indoor activities all in one place my children really had a fantastic time
16  1452010827
coventry has a lot to offer  have lived here all my life and recently moved out of coventry  reason staying in cov  was to visit friends and see the new year in museum and art gallery are fantastic  place&#39;s to visit    food and restaurants in coventry  are very good x
7   1452015663
enjoyed coventry shopping and nightlife
12  1452025926
coventry is a bustling city with historical industrial connections plenty to interest all age groups
15  1452243332
loved the cathedral and great places to dine out
15  1452255107
my visits to the historical site - especially the remnant of the church after the war
8   1452348457
easy to get around  coventry
7   1452359200
dire ugly city with ghastly ring road running round and through it no consideration for pedestrians get rid of the ring road and create a green belt around the city centre where do the cars go? create park &amp; ride facilities lovely 1960s station
5   1452431328
full of take-away shops but few good restaurants  we ate at the holyhead restaurant it was busy so we assumed it would be ok but that was clearly based on the offers rather than the quality of the food the thai green curry had clearly come out of a packet and been microwaved and the chicken pie wasn&#39;t much better with a few limp greens and a spoonful of mask to accompany it the waitress only asked if everything was ok after we&#39;d finished and she clearly wasn&#39;t interested in the answer
15  1452460741
i live  there
1   1452515686
we only had an overnight stay but did have a look around coventry and was very impressed a nice modern city with plenty of shops
2   1452625056
visited for a gig at the arches club fantastic night out warm and friendly staff
7   1452640114
herbert gallery holy trinity church pedestrianised city so easy to get around ate in bayleys new bar&#47;cocktail area highly recommend it other activities still to do x
1   1452674644
i love going to coventry but the hotel we stayed at ruined our trip there
16  1452696749
coventry transport museum litterally 1 minute walk from hotelclubs and pubs within 2 minutesfriendly staff and a good vibe around the citynice food at the hotel aswell as different restaurants around the hotel
2   1452703321
everything is to hand and some fabulous restaurants doted around for whatever food you are seeking  we went to a turkish restaurant called sultan absolutely amazing just like being in istanbul itself!
5   1452780544
city centre was better than i was led to believe
6   1452781813
coventry is a vibrant friendly place the locals were on hand a few times when i got lost and more than happy to help with directions i did not get the chance to eat out as was mostly based around my hotel and university coventry is easy to get around as most places are well signed i&#39;m due back in coventry in june and looking forward to this visit
15  1452792998
nice place
1   1452803171
we just stayed in the hotel and grounds didn&#39;t feel the need to leave on a short stay
10  1452820373
great night out in town
11  1452854152
no
4   1452940915
my trip was only to head to my exams at the university which were back to back but everything i needed was close by
18  1452953506
i was here on medical grounds so didn&#39;t get to see much
2   1452965333
it was just work based so didnt see any of the place
10  1452969831
friendly people plenty of shops and a good variety
1   1453054847
coventry was a pretty interesting place easy to get around good foodawesome experience
19  1463002573
we live local but stayed at hotel so my daughter could leave from a beautiful setting for her wedding day also family flew in from the usa &amp; stayed at the hotel coventry cathedral is worth a visit motor museum as well pesto&#39;s restaurant in wolvey is good but gets busy
11  1463056651
loads to see and do with a great shopping areas and market   plenty little cafes and pubs   we sat near a circle of water and the fountain eating ice cream   every one was friendly and every one seemed happy but it was a great sunny day !
19  1463065103
just a grey dull city nothing to do or see
10  1463066718
nice city
9   1463070710
i was only sailing through so didn&#39;t visit coventry itself
12  1463080697
coventry is a nice place in most parts
19  1463088532
we were in coventry for a graduation it would be nice if we could have spent more time there
4   1463131744
easy to get around but watch out for the traffic in the morning
0   1463213432
modern and easy to access shops  information easy to read and follow
15  1463236366
little to praise
0   1463241699
i didn&#39;t see beyond the ricoh arena as i was visiting here for a conference
14  1463254795
only went to the ricoh and that was fine
17  1463298206
coventry is full of  history different cultures good public transport  parking fees are abit expensive in the town center easy access  to the motorways
14  1463330521
overnight stop did not go into the city
5   1463345853
there on business not time for sightseeing
7   1465289503
too many roads closed which caused many traffic delays  the rocoh arena is ina  remote area  didn&#39;t understand why the local train station was closed
14  1465305088
outside the centre of the city but close to the ricoh arena which was fine to get to with a car
15  1465306458
we were there this weekend when there was a motors exhibition on so very busy we choose to visit coombe abbey in the afternoon rather walking round the city  good choice lovely country park worth a visit with a picnic
3   1465306476
good hotel if you pay the discounted rate secure parking next to hotel however they do charge now when in the past the fee was waved other hotels don&#39;t charge for parking and are just as good so worth looking round
13  1465310146
transport museum and the coventry music museum were both excellent
3   1465315485
hotel royal where we went to meet family and have lunch on sunday
19  1465316538
coventry just couldn&#39;t cope with the influx of visitors for the springsteen concert the traffic was horrendous and i missed the first hour of the concert
0   1465316609
it was a homecoming after 30years always liked coventry its redevelopmemt is inspiring and transformation the motorfest was icing on the cake
6   1465337641
extremely packed city rammed with cars on the road sides and lining the streets the cathedral is nice but same shops as in most cities not very easy to get around outside of the city centre we went to leamington spa for the day instead and it was lovely there
19  1465382294
bit of a concrete jungle but there are some gems the transport museum is worth a look and if you are prepared to wander there are some excellent pubs
17  1465396784
location not bad  other people banging their room doors was upsetting
0   1465409183
didn&#39;t see enough to make a good review
10  1465421411
very comfortable
0   1465459130
cosmos and the shopping centre
4   1465475481
our first time there beautiful place even it was in the middle of a whole heap of roadworks and refurb seemed to be a very friendly city excellent pubs and bars just walking round the streets was a pleasure very clean place as well!
4   1465478735
the shopping and weatherspoons
15  1465479479
poor inner ring road
3   1465483588
you have to visit the transport museum - free and a great way to spend an hour
7   1467312416
motor museum and cathedral
19  1467314489
we only had time to go to the arena for a concert but what we saw of coventry looked nice
1   1467314607
grew up in city watched it expand lots to see and do
13  1467375361
the modern cathedral wow go!
4   1467393429
wouldnt like to say
1   1467445679
warwick arts centre
1   1467467468
there for a funeral so my mood and view of coventry as a place should not be considered
5   1467475349
wasn&#39;t really there long enough to explore as was only down for a competition some interesting places i&#39;d have visited if i&#39;d had more time
14  1467475373
my home city so it was very nostalgic
19  1467482853
brilliant hotelsuperb shopping centre
14  1467540184
was working a few miles away didnt see much of the city
10  1467798634
fair and square
14  1467801702
pleasant centrewith some attractive shops stunning cathedralsthe modern one particularly worth everyone seeing more than once a true masterpiece
13  1467826032
casino shopping places to eat
16  1467830211
best city in the world - fact!
8   1467858256
coventry looked dirtyits almost as if while they have a lot of major projects on the go they have forgotten the regular things like cutting vergeslitter pick up etctraffic was awfulgood for shoppingdifficult to get a decent mealespecially breakfast
17  1467892058
the price and location superb and could not fault it stay there every time i go to coventry!
2   1467897377
coventry  disappointing seems very rundown
9   1467898933
the city centre is mainly flat but some of the older pretty streets may be difficult for those with impaired mobility must sees are the ruined cathedral st mary&#39;s hall and the transport museum the luftwaffe and the council knocked down much of the town but there is still a wealth of exciting architecture to see
12  1467904160
lovely and clean place   would and will come againxxx
2   1467933198
cathedral good and shopping
19  1456166777
we only stayed one night as it was my birthday treat from my wife! so we ate at coombe abbey which was great!
2   1456169893
a beautiful cathedral lots of young people around  ate mostly at nando&#39;s could not find anything more decent   getting around is easy - lots of taxis though most of them are not very clean and smelly  next time i will be more careful with a choice of a hotel   it is a good thing birmingham is so close we spent most of our time there
12  1456179936
very bland and no independent shops wouldn&#39;t recommend for shopping however surprised to find an ikea in a town centre location easy to get around as mostly traffic free bombed cathedral worth a visit
7   1456216531
this hotel was very noisy  the reception was not very welcoming and i couldn&#39;t understand him
5   1456236933
coventry deserves more recognition and appreciation than it actually does   the city and tourist venues hold great historic resources and artefacts     well prices and value for money places
13  1456238823
the beer was minty and the roads were shallow
3   1456239650
great places to eat cosmo&#39;s and nango  favourite place - the cathedral with the lovely tapestry and museum nearby  can get around easily as you can walk as it is very compact bus station is central  shopping centre is large with a nice square and an indoor shopping mall
10  1456240620
nothing to think if that was a positive experience
2   1456245090
run down rough city lots of homeless sleeping in door ways at 11 am
3   1456245730
everything was nice except the double charging from the hotel mgt
14  1456247490
i think the residents of coventry deserve a better designed city centre the concrete commercialism is a bit daunting the cathedral is wonderful but i was followed and accosted in an alley and insulted in a restaurant but once the nightmares stop i shall be fine
14  1456250464
warwick castle
14  1456303313
we stayed here because of the proximety to the nec
2   1456324707
although we stayed near coventry the reason for our visit was to go to a bull terrier dog show so we actually didn&#39;t see much of coventry itself but the old mill hotel was lovely and all the people we met were very friendly
12  1456331312
enjoyed the motor museum nice market &amp; shopping area
4   1456388259
massage at serene mind and body
14  1456420288
nice small town with historic building
9   1456422201
motor museum
3   1456427117
took the train to birmingham which was cheap spent the day looking around the expansive selection of shops and markets    wetherspoons in coventry is nice and inexpensive plenty of shops too
9   1456444671
we were there for a theatre visit &amp; restaurant
6   1456476189
shithole
18  1456582232
it was ok
19  1456589265
coventry is a city of unrivaled ugliness possibly the worst city that i&#39;ve ever visited the architecture and design of the centre is consistent in being an eye soar this isn&#39;t helped by the fact that there is very little to do the cathedral is not so nice either and there are very few desirable eating establishments we took refuge in an ikea and stayed there for most of the duration of the travel
4   1456653214
roadwork city!but pleasant and friendly-- handy for stratford and nec
3   1456654188
personally it&#39;s a city inside a big ring road of ugly flyovers the lanes on and off the flyovers cross so be alert!  reasonable steak in the weatherspoons pub next to the cinema but not signposted so found it on the second day  really good meal in the blue bistro
14  1456669910
i was up in coventry for work so did not see any attractions
4   1451399050
i live in coventry the outside is ugly but the lobby is very nice
2   1451402360
the shopping center is great not far from the ramada hotel
4   1451403859
coventry is a very interesting city and it must be seen to be appreciated  my wife and i spend considerable time during the course of each year in this city  plenty of things to do and see from a historical angle the old buildings have been preserved where possible and many new ones are interesting and inspiring    this is not a &#39;one day flit to the shops&#39; but we often shop there too  i would highly recommend a visit if you haven&#39;t been thereyou need more than one day to see it all and to enjoy it museum of transport; huge indoor market open all the time; ice skating rink; huge 6 floor ikea; parks; cathedral; ancient buildings etcall worth a visit!!!  plenty of good quality reasonably priced eating placesa must if you have a hungry family!    it&#39;s easy to get around if you haven&#39;t your own transport; simply pick up a bus timetable or use your smart phone   avoid the need to use the main hospital as it can take you an &#39;hour plus&#39; to get out of the car parks at the close of visiting times bad news!  it is quite a way out from the centre of town  there is a brilliant (if not a bit scary) ring road that circles the city  if you miss your pull-off then you can go right round the whole ring and be back where you started within about 45 minutes  i would rather drive around marble arch hyde park corner park lane in london in the rush hour than drive around the coventry ring roadbut i suppose it&#39;s what you get used to doingand i&#39;m over 71 years of age  coventry?  don&#39;t drive past itgo there!!!
9   1451408331
love coventry but i don&#39;t get time to check it out as i only come for work
11  1451476026
park and ride is unreliable and no longer a dedicated serviceieit uses buses from other routes to call at the memorial park to pick up passengers for the citythese coaches are often full and late!
10  1451572722
totul perfect nimic neregula multumesc
9   1451576165
for work
13  1451799312
the fields an open ness that calms me which i love
10  1451827425
the hotel is situated close to the centre but then again most hotels are as coventry is not that big it&#39;s good to be able to walk everywhere and not worry about parking
10  1451840440
the premier in on earsldon road was lovely hana moon a new and local japanese restaurant was just right for a nice meal out the herbert art gallery &#47; museum was good for a day out
11  1451846187
we love the local pubs for food the place is easy to get around and has some amazingly historic and beautiful buildings lovely countryside and friendly people always relaxing and i never want to leave
19  1451855540
visit to see family at christmas
8   1451856507
this was a trip that we make on a regular basis to visit family
6   1451860931
we visit our son who went to coventry university great place to study is being much improved good restaurants and places to visit
9   1462296450
cathedral the best in the world
16  1462296731
stayed for family party locally
6   1462296829
don&#39;t do it spend a little more and go else where
18  1462297810
i was born there and my family still live there thats the only reason why i go back it&#39;s very historical and steeped in history and worth a visit i had a great upbringing there i don&#39;t think the night life is great if you like a drink and a choice of bars you shouldn&#39;t pick here
12  1462298018
it was for work
14  1462298695
ok smaller than expected!
 */

/*
9
4
0
5
9
0
13
2
18
 */