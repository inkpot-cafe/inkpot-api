package com.inkpot.core.application

import com.inkpot.core.application.port.service.DocumentService

interface CoreContext {
    fun documentService(): DocumentService
}