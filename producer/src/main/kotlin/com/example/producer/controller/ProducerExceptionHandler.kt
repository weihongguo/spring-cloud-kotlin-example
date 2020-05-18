package com.example.producer.controller

import com.example.security.BaseExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class ProducerExceptionHandler : BaseExceptionHandler()