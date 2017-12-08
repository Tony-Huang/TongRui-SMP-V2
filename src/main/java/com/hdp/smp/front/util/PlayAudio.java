package com.hdp.smp.front.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import sun.audio.AudioPlayer;
import sun.audio.AudioStream;

public class PlayAudio {
    public PlayAudio() {
        super();
    }

    public void play(String Filename) {
        try {
            InputStream in = new FileInputStream(Filename); //FIlename ������ص������ļ��磨��game.wav����
            // ���������д���һ��AudioStream����

            AudioStream as = new AudioStream(in);
            AudioPlayer.player.start(as); //�þ�̬��Աplayer.start��������

            //AudioPlayer.player.stop(as);//�ر����ֲ���
            //���Ҫʵ��ѭ�����ţ��������������ȡ������ġ�AudioPlayer.player.start(as);�����
            /*   AudioData data = as.getData();
    ����ContinuousAudioDataStream gg= new ContinuousAudioDataStream (data);
    ����AudioPlayer.player.start(gg);// Play audio.
    ����*/

            //���Ҫ��һ�� URL ��Ϊ��������Դ(source)����������Ĵ�����ʾ�滻��������������������
            /*AudioStream as = new AudioStream (url.openStream());*/

        } catch (FileNotFoundException e) {

            System.out.print("FileNotFoundException ");

        } catch (IOException e) {
            e.printStackTrace();

            System.out.println("�д���");

        }

    }

    public static void main(String[] args) {
        System.out.println("----- start playing ------");
        
        PlayAudio pa = new PlayAudio();
        pa.play("C:\\JDeveloper\\mywork\\MyUI\\WebUI\\public_html\\igms\\alarm.wav");
        
        pa.play("C:\\JDeveloper\\mywork\\MyUI\\WebUI\\public_html\\igms\\enter.wav");

    }
}
