package cobo.file.repository

import cobo.file.data.entity.File
import org.springframework.data.jpa.repository.JpaRepository

interface FileRepository: JpaRepository<File, Int> {
}