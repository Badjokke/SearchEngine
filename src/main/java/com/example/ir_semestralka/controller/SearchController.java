package com.example.ir_semestralka.controller;

import com.example.ir_semestralka.utils.JSONBuilder;
import com.example.ir_semestralka.utils.Log;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.logging.Level;

//todo napsat tridu obalujici query request pro automatickou serializaci a pohodlnost
@RestController
//todo lepe
//todo dodelat backend obecne, zatim tady simuluju loading, abych ozkousel hooky v reactu
@CrossOrigin(origins = "http://localhost:3000")
public class SearchController {
    @RequestMapping(method = RequestMethod.GET,value = "/search",produces = "application/json")
    public String searchQuery(@RequestParam Optional<String> query){
        Log.log(Level.INFO,"Query search recieved");
        try{
            Thread.sleep(5000);
        }
        catch(InterruptedException exception){
            System.out.println("kdo me vzbudil??");
        }


        return "{\"articles\":["+
                "{" +
                "  \"title\": \"Ukraine war: Zelensky honours unarmed soldier filmed being shot\"," +
                "  \"author\": \"Matt Murphy\"," +
                "  \"news_category\": \"War in Ukraine\"," +
                "  \"content\": \"President Volodymyr Zelensky has honoured a captured Ukrainian soldier who was filmed apparently being killed by Russian troops last week.\\n\\nOleksandr Matsievskiy was unarmed when he was videoed smoking a cigarette and shouting \\\"Glory to Ukraine\\\" before being gunned down.\\n\\nThe footage has seen Moscow faced with fresh allegations of war crimes.\\n\\nOn Sunday, President Zelensky awarded the 42-year-old with the Hero of Ukraine - the country's highest honour.\\n\\n\\\"Today, I have bestowed the title of Hero of Ukraine on soldier Oleksandr Matsievskiy,\\\" Mr Zelensky said in his nightly address from Kyiv.\\n\\n\\\"A man that all Ukrainians will know. A man who will be forever remembered. For his bravery, for his confidence in Ukraine and for his 'Glory to Ukraine'.\\\"\\n\\nMatsievskiy, a sniper serving with a unit from the northern region of Chernihiv, is believed to have been killed at the end of last year while fighting in the eastern city of Bakhmut which Russia has been trying to capture for months in a grinding war of attrition.\\n\\nThe soldier's final words, \\\"Glory to Ukraine\\\", became popular among ordinary Ukrainians after Russia annexed Ukraine's southern Crimea peninsula in 2014. It has seen a resurgence after Moscow's full-scale invasion launched last year.\\n\\nIn the footage, one of the shooters - believed to be a Russian soldier - is heard saying \\\"die\\\" and using an expletive after the prisoner of war (POW) is shot dead.\\n\\nConfusion initially reigned over the soldier's identity, and he was initially misidentified by the Ukrainian military as Tymofiy Shadura, another missing soldier who had been fighting in Bakhmut\\n\\nUkraine's authorities have said the killing is \\\"another proof this war is genocidal\\\" and launched a criminal investigation, vowing to hunt down the perpetrators.\\n\\nRussia has not publicly commented on the video.\\n\\nIn a statement issued on Monday, the Moldovan foreign ministry said Matsievskiy was also a Moldovan citizen and condemned his killing, accusing Moscow of war committing war crimes.\\n\\nKyiv and its Western allies have accused Russian troops of committing mass war crimes - including torture, rape and murder - since President Vladimir Putin ordered the invasion. Russia denies the allegations.\\n\\nLast week, in an interview with the German newspaper Bild, Matsievskiy's mother identified her son and said he had worked as an electrician in Ukrainian capital Kyiv before the war.\\n\\n\\\"He stood there without weapons, but at the same time, he was proud to be Ukrainian,\\\" she told the newspaper.\\n\\n\\\"He was always incredibly courageous. At that moment, the only weapon with which he could defend himself was saying: Slava Ukraini [Glory to Ukraine]!\\\"\"\n" +
                "}," +
                "{" +
                "  \"title\": \"Ukraine war: Heavy losses reported as battle for Bakhmut rages\"," +
                "  \"author\": \"George Wright\"," +
                "  \"news_category\": \"War in Ukraine\"," +
                "  \"content\": \"Ukraine and Russia have reported inflicting heavy losses as the battle for Bakhmut rages on.Moscow has been trying to take the eastern Ukrainian city for months in a grinding war of attrition.\\n\\nUkrainian President Volodymyr Zelensky said Russian forces had suffered more than 1,100 deaths in the past few days, with many more seriously injured.\\n\\nRussia said it had killed more than 220 Ukrainian service members over the past 24 hours.\\n\\nThe BBC is unable to verify the numbers given by either side.\\n\\nAnalysts say Bakhmut has little strategic value, but has become a focal point for Russian commanders who have struggled to deliver any positive news to the Kremlin.\\n\\nCapture of the city would bring Russia slightly closer to its goal of controlling the whole of Donetsk region, one of four regions in eastern and southern Ukraine annexed by Russia last September following referendums widely condemned outside Russia as a sham.\\n\\nUkrainian commanders, who have committed significant resources to defending the city, say their strategy aims to tie Russia's forces down and prevent Moscow from launching any further offensives in the coming months.\\n\\nWhy Bakhmut matters for Russia and Ukraine\\n\\\"In less than a week, starting from 6 March, we managed to kill more than 1,100 enemy soldiers in the Bakhmut sector alone, Russia's irreversible loss, right there, near Bakhmut,\\\" Mr Zelensky said in his nightly video address.\\n\\nHe added that 1,500 Russian soldiers were wounded badly enough to keep them out of further action.\\n\\nRussia's defence ministry said Russian forces had killed \\\"more than 220 Ukrainian servicemen\\\".\\n\\nThe commander of Ukraine's ground forces, Col Gen Oleksandr Syrskyi, said the Russian mercenary Wagner Group was attacking his troops from several directions in a bid to break through defences and advance to the central districts of the town.\\n\\nThe paramilitary organisation is at the heart of the Russian assault on Bakhmut. Its leader, Yevgeny Prigozhin, has staked his reputation, and that of his private army, on seizing Bakhmut.\\n\\nHe said on Sunday that the situation in the city was \\\"difficult, very difficult, the enemy is fighting for every metre\\\".\\n\\n\\\"And the closer to the city centre, the fiercer the fighting,\\\" he said in a voice recording published on Telegram.\\n\\nAfter his envisioned capture of Bakhmut, \\\"we will begin to reboot\\\" and \\\"will start recruiting new people from the regions\\\", he said.\\n\\nAnd on Saturday, the Institute for the Study of War - a US think tank - reported that Moscow's offence was stalling.\\n\\n\\\"Wagner Group fighters are likely becoming increasingly pinned in urban areas... and are therefore finding it difficult to make significant advances,\\\" it said.There were about 70,000 people living in Bakhmut before the invasion, but only a few thousand remain. The city was once best known for its salt and gypsum mines and huge winery.\\n\\nThose who remain in the city risk a hazardous existence, with four people injured in Bakhmut on Monday, Donetsk regional governor Pavlo Kyrylenko said.\\n\\nLike Russia, Ukraine has also given Bakhmut political significance, with President Zelensky making the city an emblem of resistance.\\n\\nWhen he visited Washington in December, he called it \\\"the fortress of our morale\\\" and gave a Bakhmut flag to the US Congress.\\n\\nWestern officials estimate between 20,000 and 30,000 Russian troops have been killed or injured so far in and around Bakhmut.\\n\\nA draft law introduced in the Russian parliament on Monday aims to push back the age bracket for compulsory military service, from the current 18-27 years to 21-30.\\n\\nReuters reported that, due to the transition period between the old legislation and the new, 2024 and 2025 would see the conscription age span 10 or 11 years rather than the usual nine - meaning more men would be eligible to fight.\\n\\nRussia's previous attempt to draft thousands of new recruits into the Ukraine war met with some resistance. In September the announcement of a partial military mobilisation saw long queues form at border crossings as men of draft age sought to flee the call-up.\\n\\nThe Kremlin said reports of fighting-age men fleeing had been exaggerated.\\n\\nBesides the Bakhmut fighting, seven residents were injured elsewhere in Donetsk region on Monday, governor Pavlo Kyrylenko said.\\n\\nFurther east in Luhansk, regional governor Serhiy Haidai said the Russians had \\\"significantly intensified shelling\\\" on the front line. He added that Russia was bringing more and more equipment and troops to the area.\\n\\nElsewhere in eastern Ukraine, there were 47 attacks on Ukrainian troops in Zaporizhzhia, according to the regional administration.\"\n" +
                "}" +
                "\n" + "]"+
                "}";


    }

}
