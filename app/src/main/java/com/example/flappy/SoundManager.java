package com.example.flappy;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;

import java.util.HashMap;

public class SoundManager {
    private SoundPool m_SoundPool; //안드로이드에서 지원하는 사운드풀
    private HashMap m_SoundPoolMap; //사운드 id를 저장할 해시맵
    private AudioManager m_AudioManager; //사운드 매니져
    private Context m_Activity; //어플의 컨텍스트 값

    public void Init(Context context){
        m_SoundPool = new SoundPool(4, AudioManager.STREAM_MUSIC, 0); //사운드풀 생성
        m_SoundPoolMap = new HashMap(); //해시맵 생성
        m_AudioManager = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
        m_Activity = context; //액티비티 핸들값 가져옴
    }

    private static SoundManager m_instance;

    public static SoundManager getInstance(){
        if(m_instance == null){
            m_instance = new SoundManager();
        }
        return m_instance;
    }

    public void addSound(int index, int soundID){
        int id = m_SoundPool.load(m_Activity, soundID, 1);//해시맵에서 사운드 로드
        m_SoundPoolMap.put(index, id);
    }

    public void play(int index){
        float streamVolume = m_AudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        streamVolume = streamVolume / m_AudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        m_SoundPool.play((Integer)m_SoundPoolMap.get(index), streamVolume, streamVolume, 1, 0, 1f);
    }

    public void playLooped(int index){
        float streamVolume = m_AudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        streamVolume = streamVolume / m_AudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        m_SoundPool.play((Integer)m_SoundPoolMap.get(index), streamVolume, streamVolume, 1, -1, 1f);
    }

}
