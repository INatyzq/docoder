package cn.yangzq.docoder.content.maputil;

import cn.yangzq.docoder.content.entity.NoteDir;
import cn.yangzq.docoder.content.form.NoteDirForm;
import org.mapstruct.Mapper;

/**
*@author yangzq
*@description 表单到PO的转换Mapper
**/
@Mapper(componentModel = "spring")
public interface FormToPoMapper {

    NoteDir toNoteDir(NoteDirForm form);

}
