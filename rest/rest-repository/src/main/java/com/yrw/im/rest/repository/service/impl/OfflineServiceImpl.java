package com.yrw.im.rest.repository.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yrw.im.common.domain.po.Offline;
import com.yrw.im.common.exception.ImException;
import com.yrw.im.proto.constant.MsgTypeEnum;
import com.yrw.im.proto.generate.Ack;
import com.yrw.im.proto.generate.Chat;
import com.yrw.im.rest.repository.mapper.OfflineMapper;
import com.yrw.im.rest.repository.service.OfflineService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Date: 2019-05-05
 * Time: 09:49
 *
 * @author yrw
 */
@Service
public class OfflineServiceImpl extends ServiceImpl<OfflineMapper, Offline> implements OfflineService {

    @Override
    public void saveChat(Chat.ChatMsg msg) {
        Offline offline = new Offline();
        offline.setMsgId(msg.getId());
        offline.setMsgCode(MsgTypeEnum.CHAT.getCode());
        offline.setToUserId(msg.getDestId());
        offline.setContent(msg.toByteArray());

        saveOffline(offline);
    }

    @Override
    public void saveAck(Ack.AckMsg msg) {
        Offline offline = new Offline();
        offline.setMsgId(msg.getId());
        offline.setMsgCode(MsgTypeEnum.getByClass(Ack.AckMsg.class).getCode());
        offline.setToUserId(msg.getDestId());
        offline.setContent(msg.toByteArray());

        saveOffline(offline);
    }

    private void saveOffline(Offline offline) {
        if (!save(offline)) {
            throw new ImException("[offline] save chat msg failed");
        }
    }

    @Override
    public List<Offline> listOffline(Long userId) {
        return list(new LambdaQueryWrapper<Offline>()
            .eq(Offline::getToUserId, userId)
            .orderBy(true, true, Offline::getMsgId));
    }
}
