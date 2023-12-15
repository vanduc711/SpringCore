package com.example.springbatch.processor;

import com.example.springbatch.model.Infomation;
import org.springframework.batch.item.ItemProcessor;

//InfoItemProcessor mở rộng interface ItemProcessor
//Interface ItemProcessor định nghĩa một phương thức process
//InfoItemProcessor thực hiện xử lý mỗi mục dữ liệu
//ở đây trả về dữ liệu đầu vào mà không xử lý.
public class InfoItemProcessor implements ItemProcessor<Infomation, Infomation> {
    @Override
    public Infomation process(Infomation item) throws Exception {
        return item;
    }
}
