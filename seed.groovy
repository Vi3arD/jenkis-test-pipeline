 freeStyleJob('seed-git') {
                      displayName('seed-git')
                      parameters {
                          stringParam('FOLDER_NAME', 'jobs')
                          stringParam('NAME_DEPLOY')
                          stringParam('NAME_DESTROY')
                          stringParam('BASE_PATH_DEPLOY')
                          stringParam('BASE_PATH_DESTROY')
                          stringParam('BRANCH', 'master')
                          stringParam('SCRIPT', 'Jenkinsfile')
                      }
                      label('k8s-agent')
                      steps {
                          dsl {
                              text('''
                                      folder("$FOLDER_NAME")
                                      pipelineJob("$FOLDER_NAME/$NAME_DEPLOY") {
                                        displayName("$NAME_DEPLOY")
                                        parameters {
                                            stringParam('AWS_REGION')
                                            stringParam('BUCKET_NAME', 'cloud-control-gitlab')
                                            stringParam('CALLBACK_URL')
                                            stringParam('WORKSPACE_ID')
                                        }
                                        definition {
                                            cpsScm {
                                                scm {
                                                    git {
                                                        configure { git ->
                                                            git / 'extensions' / 'hudson.plugins.git.extensions.impl.SparseCheckoutPaths' / 'sparseCheckoutPaths' {
                                                                'hudson.plugins.git.extensions.impl.SparseCheckoutPath' {
                                                                    path("$BASE_PATH_DEPLOY")
                                                                }
                                                            }
                                                        }
                                                        branch("$BRANCH")
                                                        remote {
                                                            credentials('jenkins_user')
                                                            url('https://github.com/Vi3arD/jenkis-test-pipeline.git')
                                                        }
                                                    }
                                                }
                                                scriptPath("$BASE_PATH_DEPLOY/$SCRIPT")
                                            }
                                        }
                                      }
                                      pipelineJob("$FOLDER_NAME/$NAME_DESTROY") {
                                        displayName("$NAME_DESTROY")
                                        parameters {
                                            stringParam('BUCKET_NAME', 'cloud-control-gitlab')
                                        }
                                        definition {
                                            cpsScm {
                                                scm {
                                                    git {
                                                        configure { git ->
                                                            git / 'extensions' / 'hudson.plugins.git.extensions.impl.SparseCheckoutPaths' / 'sparseCheckoutPaths' {
                                                                'hudson.plugins.git.extensions.impl.SparseCheckoutPath' {
                                                                    path("$BASE_PATH_DESTROY")
                                                                }
                                                            }
                                                        }
                                                        branch("$BRANCH")
                                                        remote {
                                                            credentials('jenkins_user')
                                                            url('https://github.com/Vi3arD/jenkis-test-pipeline.git')
                                                        }
                                                    }
                                                }
                                                scriptPath("$BASE_PATH_DESTROY/$SCRIPT")
                                            }
                                        }
                                      }
                              ''')
                          }
                      }
                      properties {
                          rebuild {
                              rebuildDisabled()
                          }
                      }
                  }
